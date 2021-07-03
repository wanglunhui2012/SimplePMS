package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import indi.simple.pms.constant.SystemRedisConstant;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.SystemUserDO;
import indi.simple.pms.entity.dataobject.SystemUserJobDO;
import indi.simple.pms.entity.dataobject.SystemUserRoleDO;
import indi.simple.pms.entity.datatransferobject.SystemChangePasswordDTO;
import indi.simple.pms.entity.datatransferobject.SystemUserSearchDTO;
import indi.simple.pms.entity.datatransferobject.SystemUserUpdateProfileDTO;
import indi.simple.pms.exception.BadRequestException;
import indi.simple.pms.mapper.SystemUserMapper;
import indi.simple.pms.service.*;
import indi.simple.pms.support.file.LocalFileTemplate;
import indi.simple.pms.util.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 系统用户表 system_user 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:36
 */
@Service()
@RequiredArgsConstructor
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUserDO> implements SystemUserService {
    @Value("${jwt.system.header}")
    private String jwtSystemHeader;
    @Value("${spring.security.default-password}")
    private String defaultPassword;
    private final SystemUserRoleService systemUserRoleService;
    private final SystemRoleService systemRoleService;
    private final SystemUserJobService systemUserJobService;
    private final SystemJobService systemJobService;
    @Autowired
    private SystemPermissionService systemPermissionService;
    private final RedisUtil redisUtil;
    @Autowired
    private SystemDepartmentService systemDepartmentService;
    private final LocalFileTemplate localFileTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean add(String json,MultipartFile avatar) {
        LocalDateTime localDateTime = LocalDateTime.now();
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();

        SystemUserDO systemUserDO = JsonUtil.jsonToObject(json, SystemUserDO.class);// 解析json

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();// 构建Validator实例
        Set<ConstraintViolation<SystemUserDO>> validates=validator.validate(systemUserDO);// 调用校验方法
        if(!validates.isEmpty()){
            throw new RuntimeException(validates.iterator().next().getMessage());// 取第一个错误信息返回，快速失败模式
        }

        if (super.count(Wrappers.<SystemUserDO>lambdaQuery().eq(SystemUserDO::getName, systemUserDO.getName())) > 0) {
            throw new BadRequestException("用户名已存在!");
        } else if (SystemPermissionUtil.isRoleBeyond(systemUserDO.getRoleIdList())) {
            throw new BadRequestException("新增用户所选角色,超越当前用户的角色范围!");
        } else {
            systemUserDO.setSalt(RandomStringUtils.randomAlphanumeric(20));
            systemUserDO.setPassword((new BCryptPasswordEncoder()).encode(this.defaultPassword + systemUserDO.getSalt()));
            systemUserDO.setCreateUserId(jwtUserBO.getId());
            systemUserDO.setCreateDepartmentId(jwtUserBO.getDepartmentId());
            systemUserDO.setCreateTime(localDateTime);
            systemUserDO.setUpdateTime(localDateTime);
            boolean flag = super.save(systemUserDO); // 必须先保存才会回填id

            String avatarUrl=this.uploadAvatar(systemUserDO.getId(),avatar);
            systemUserDO.setAvatarUrl(avatarUrl);

            systemUserDO.getRoleIdList().stream().forEach((roleId) -> {
                SystemUserRoleDO systemUserRoleDO = new SystemUserRoleDO();
                systemUserRoleDO.setUserId(systemUserDO.getId());
                systemUserRoleDO.setRoleId(roleId);
                this.systemUserRoleService.save(systemUserRoleDO);
            });
            systemUserDO.getJobIdList().stream().forEach((jobId) -> {
                SystemUserJobDO systemUserJobDO = new SystemUserJobDO();
                systemUserJobDO.setJobId(jobId);
                systemUserJobDO.setUserId(systemUserDO.getId());
                this.systemUserJobService.save(systemUserJobDO);
            });

            return flag;
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean delete(List<Long> idList) {
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        if (idList.contains(jwtUserBO.getId())) {
            throw new BadRequestException("不能删除自己!");
        } else {
            this.systemUserRoleService.remove(Wrappers.<SystemUserRoleDO>lambdaQuery().in(SystemUserRoleDO::getUserId, idList));
            this.systemUserJobService.remove(Wrappers.<SystemUserJobDO>lambdaQuery().in(SystemUserJobDO::getUserId, idList));

            // 必须在删除前获取，否则拿到的为空
            List<SystemUserDO> systemUserDOList=super.listByIds(idList);

            boolean flag = super.removeByIds(idList);

            // 删除头像文件和缓存
            String defaultAvatarName=localFileTemplate.getDefaultAvatarName();
            for (SystemUserDO systemUserDO:systemUserDOList){
                String avatarUrl=systemUserDO.getAvatarUrl();
                if(StringUtil.isNotEmpty(avatarUrl)){
                    String avatarName=avatarUrl.substring(avatarUrl.lastIndexOf('/')+1);
                    if(!avatarName.equals(defaultAvatarName)){ // 不能删除默认头像
                        localFileTemplate.delete(avatarName);
                    }
                }
                this.redisUtil.keyDelete(SystemRedisConstant.LOGIN_JWTUSER+SystemRedisConstant.SPLIT+systemUserDO.getName());
            }

            return flag;
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(String json,MultipartFile newAvatar) {
        LocalDateTime localDateTime = LocalDateTime.now();

        SystemUserDO systemUserDO = JsonUtil.jsonToObject(json, SystemUserDO.class);// 解析json

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();// 构建Validator实例
        Set<ConstraintViolation<SystemUserDO>> validates=validator.validate(systemUserDO, SystemUserDO.Update.class);// 调用校验方法
        //Set<ConstraintViolation<ProjectBelow80>> validates=validator.validateProperty(projectInvestmentFilingDO,"id");// 只校验一个字段
        if(!validates.isEmpty()){
            throw new RuntimeException(validates.iterator().next().getMessage());// 取第一个错误信息返回，快速失败模式
        }

        if (super.count(Wrappers.<SystemUserDO>lambdaQuery().eq(SystemUserDO::getName, systemUserDO.getName()).ne(SystemUserDO::getId, systemUserDO.getId())) > 0) {
            throw new BadRequestException("用户名已存在!");
        } else if (SystemPermissionUtil.isRoleBeyond(systemUserDO.getRoleIdList())) {
            throw new BadRequestException("修改用户所选角色,超越当前用户的角色范围!");
        } else {
            this.systemUserRoleService.remove(Wrappers.<SystemUserRoleDO>lambdaQuery().eq(SystemUserRoleDO::getUserId, systemUserDO.getId()));
            this.systemUserJobService.remove(Wrappers.<SystemUserJobDO>lambdaQuery().eq(SystemUserJobDO::getUserId, systemUserDO.getId()));

            systemUserDO.getRoleIdList().stream().forEach((roleId) -> {
                SystemUserRoleDO systemUserRoleDO = new SystemUserRoleDO();
                systemUserRoleDO.setUserId(systemUserDO.getId());
                systemUserRoleDO.setRoleId(roleId);
                this.systemUserRoleService.save(systemUserRoleDO);
            });
            systemUserDO.getJobIdList().stream().forEach((jobId) -> {
                SystemUserJobDO systemUserJobDO = new SystemUserJobDO();
                systemUserJobDO.setJobId(jobId);
                systemUserJobDO.setUserId(systemUserDO.getId());
                this.systemUserJobService.save(systemUserJobDO);
            });
            systemUserDO.setUpdateTime(localDateTime);
            String rawUserName=this.getNameById(systemUserDO.getId()); // 获取原始用户名，必须放在更新前面，否则更新后用户名就变了
            boolean flag = super.updateById(systemUserDO);

            // 如果上传了新头像，则需要修改头像
            if(newAvatar!=null&&!newAvatar.isEmpty()){// 文件必须不为空，如上传的文件大小为0kb则file.isEmpty()将为true
                this.uploadAvatar(systemUserDO.getId(),newAvatar);
            }
            // 删除缓存
            this.redisUtil.keyDelete(SystemRedisConstant.LOGIN_JWTUSER+SystemRedisConstant.SPLIT+rawUserName);

            return flag;
        }
    }

    @Override
    public IPage<SystemUserDO> searchPage(SystemUserSearchDTO systemUserSearchDTO) {
        Long pageNum = Optional.ofNullable(systemUserSearchDTO.getPageNum()).orElse(0L);
        Long pageSize = Optional.ofNullable(systemUserSearchDTO.getPageSize()).orElse(-1L);

        if(!CollectionUtils.isEmpty(systemUserSearchDTO.getDepartmentIdList())){
            systemUserSearchDTO.getDepartmentIdList().addAll(this.systemDepartmentService.getChildrenIdListRecursionByIdList(systemUserSearchDTO.getDepartmentIdList()));
        }

        IPage<SystemUserDO> systemUserDOIPage = super.baseMapper.searchPage(new Page<>(pageNum, pageSize), systemUserSearchDTO);

        systemUserDOIPage.getRecords().stream().forEach(systemUserDO -> {
            systemUserDO.setRoleIdList(this.systemRoleService.getRoleIdListByUserId(systemUserDO.getId()));
            systemUserDO.setJobIdList(this.systemJobService.getJobIdListByUserId(systemUserDO.getId()));
        });

        return systemUserDOIPage;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean changePassword(SystemChangePasswordDTO changePasswordDTO, HttpServletRequest request) {
        LocalDateTime localDateTime = LocalDateTime.now();
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getAgainNewPassword())) {
            throw new BadRequestException("修改失败，两次密码不一致!");
        } else if (!passwordEncoder.matches(changePasswordDTO.getOldPassword() + jwtUserBO.getSalt(), jwtUserBO.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误!");
        } else if (changePasswordDTO.getNewPassword().equals(changePasswordDTO.getOldPassword())) {
            throw new BadRequestException("修改失败，新密码不能与旧密码相同!");
        } else {
            boolean flag = SqlHelper.retBool(super.baseMapper.changePassword(passwordEncoder.encode(changePasswordDTO.getNewPassword() + jwtUserBO.getSalt()), localDateTime, jwtUserBO.getId()));

            // 删除缓存
            this.redisUtil.keyDelete(SystemRedisConstant.LOGIN_JWTUSER+SystemRedisConstant.SPLIT+jwtUserBO.getName());

            return flag;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(Long userId, MultipartFile file) {
        String result;
        String fileDownloadUrlPrefix=localFileTemplate.getFileDownloadUrlPrefix();
        String defaultAvatarName=localFileTemplate.getDefaultAvatarName();
        String endpoint=localFileTemplate.getEndpoint();
        if (file != null && !file.isEmpty()) {
            String newFileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            result=endpoint+fileDownloadUrlPrefix+newFileName;
            String rawAvatarUrl=this.getAvatarUrl(userId); // 在更新前获取，否则为更新后的
            super.update(Wrappers.<SystemUserDO>lambdaUpdate().eq(SystemUserDO::getId, userId).set(SystemUserDO::getAvatarUrl,result));
            // 文件操作放在数据库操作之后，可以回滚
            try {

                if(StringUtil.isEmpty(rawAvatarUrl)){ // 如果用户没有头像直接放入
                    localFileTemplate.upload(newFileName,file.getInputStream());
                }else{ // 如果有头像，先删除，再放入
                    String avatar=rawAvatarUrl.substring(rawAvatarUrl.lastIndexOf('/')+1);
                    if(!avatar.equals(defaultAvatarName)){ // 不能删除默认头像
                        localFileTemplate.delete(avatar);
                    }

                    localFileTemplate.upload(newFileName,file.getInputStream());
                }
            } catch (Exception e) {
                throw new RuntimeException("服务器文件写入失败!",e);
            }
        } else {
            result=endpoint+fileDownloadUrlPrefix+defaultAvatarName;
            super.update(Wrappers.<SystemUserDO>lambdaUpdate().eq(SystemUserDO::getId, userId).set(SystemUserDO::getAvatarUrl,result));
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProfile(SystemUserUpdateProfileDTO systemUserUpdateProfileDTO) {
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();

        boolean flag=super.update(Wrappers.<SystemUserDO>lambdaUpdate()
                .set(SystemUserDO::getNickName,systemUserUpdateProfileDTO.getNickName())
                .set(SystemUserDO::getEmail,systemUserUpdateProfileDTO.getEmail())
                .set(SystemUserDO::getPhoneNumber,systemUserUpdateProfileDTO.getPhoneNumber())
                .set(SystemUserDO::getSex,systemUserUpdateProfileDTO.getSex())
                .set(SystemUserDO::getRemark,systemUserUpdateProfileDTO.getRemark())
                .eq(SystemUserDO::getId,jwtUserBO.getId()));

        return flag;
    }

    @Override
    public SystemUserDO getCurrent() {
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        SystemUserDO systemUserDO = super.getById(jwtUserBO.getId());
        systemUserDO.setDepartmentName(this.systemDepartmentService.getById(systemUserDO.getDepartmentId()).getName());
        List<Long> roleIdList = this.systemUserRoleService.list(Wrappers.<SystemUserRoleDO>lambdaQuery().eq(SystemUserRoleDO::getUserId, systemUserDO.getId())).stream().map(SystemUserRoleDO::getRoleId).collect(Collectors.toList());
        systemUserDO.setRoleIdList(roleIdList);
        List<Long> jobIdList = this.systemUserJobService.list(Wrappers.<SystemUserJobDO>lambdaQuery().eq(SystemUserJobDO::getUserId, systemUserDO.getId())).stream().map(SystemUserJobDO::getJobId).collect(Collectors.toList());
        systemUserDO.setJobIdList(jobIdList);

        return systemUserDO;
    }

    @Override
    public List<SystemUserDO> getByJobId(Long jobId) {
        return super.baseMapper.getByJobId(jobId);
    }

    @Override
    public String getAvatarUrl(Long userId) {
        return super.baseMapper.getAvatarUrl(userId);
    }

    @Override
    public String getNameById(Long userId) {
        return super.baseMapper.getNameById(userId);
    }

    @Override
    public List<String> getNameListByDepartmentId(List<Long> departmentIdList) {
        return super.baseMapper.getNameListByDepartmentId(departmentIdList);
    }
}
