package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.constant.SystemRedisConstant;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.SystemPermissionDO;
import indi.simple.pms.entity.dataobject.SystemRolePermissionDO;
import indi.simple.pms.entity.dataobject.SystemUserRoleDO;
import indi.simple.pms.entity.datatransferobject.SystemPermissionSearchDTO;
import indi.simple.pms.exception.BadRequestException;
import indi.simple.pms.mapper.SystemPermissionMapper;
import indi.simple.pms.service.SystemPermissionService;
import indi.simple.pms.service.SystemRolePermissionService;
import indi.simple.pms.service.SystemUserRoleService;
import indi.simple.pms.service.SystemUserService;
import indi.simple.pms.util.RedisUtil;
import indi.simple.pms.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 系统权限表 system_permission 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:23
 */
@Service("systemPermissionService")
public class SystemPermissionServiceImpl extends ServiceImpl<SystemPermissionMapper, SystemPermissionDO> implements SystemPermissionService {
    @Autowired
    private SystemRolePermissionService systemRolePermissionService;
    @Autowired
    private SystemUserRoleService systemUserRoleService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean add(SystemPermissionDO systemPermissionDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        if (super.count(Wrappers.<SystemPermissionDO>lambdaQuery().eq(SystemPermissionDO::getName, systemPermissionDO.getName())) > 0) {
            throw new BadRequestException("权限名已存在!");
        } else {
            systemPermissionDO.setCreateUserId(jwtUserBO.getId());
            systemPermissionDO.setCreateDepartmentId(jwtUserBO.getDepartmentId());
            systemPermissionDO.setCreateTime(localDateTime);
            systemPermissionDO.setUpdateTime(localDateTime);
            boolean flag = super.save(systemPermissionDO);
            return flag;
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean delete(List<Long> idList) {
        // 必须放在删除前，否则下面删除后这里为空
        List<Long> roleIdList=this.systemRolePermissionService.list(Wrappers.<SystemRolePermissionDO>lambdaQuery().in(SystemRolePermissionDO::getPermissionId,idList)).stream().map(SystemRolePermissionDO::getRoleId).collect(Collectors.toList());

        // 删除角色权限
        this.systemRolePermissionService.remove(Wrappers.<SystemRolePermissionDO>lambdaQuery().in(SystemRolePermissionDO::getPermissionId, idList));
        boolean flag=super.removeByIds(idList);

        // permission_id->role_id->user_id，再删除缓存
        List<Long> userIdList=this.systemUserRoleService.list(Wrappers.<SystemUserRoleDO>lambdaQuery().in(SystemUserRoleDO::getRoleId,roleIdList)).stream().map(SystemUserRoleDO::getUserId).collect(Collectors.toList());
        for (Long userId:userIdList){
            String username=this.systemUserService.getNameById(userId);
            this.redisUtil.keyDelete(SystemRedisConstant.LOGIN_JWTUSER+SystemRedisConstant.SPLIT+username);
        }

        return flag;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(SystemPermissionDO systemPermissionDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (super.count(Wrappers.<SystemPermissionDO>lambdaQuery().eq(SystemPermissionDO::getName, systemPermissionDO.getName()).ne(SystemPermissionDO::getId, systemPermissionDO.getId())) > 0) {
            throw new BadRequestException("权限名已存在!");
        } else {
            systemPermissionDO.setUpdateTime(localDateTime);
            String rawName=this.getNameById(systemPermissionDO.getId()); // 必须在修改前获取，否则拿到的是修改后的不是原先的
            boolean flag = super.updateById(systemPermissionDO);

            // 因为JwtUser中缓存的是权限名，所以如果修改了权限名，就要修改缓存
            if (!rawName.equals(systemPermissionDO.getName())){
                // permission_id->role_id->user_id，再删除缓存
                List<Long> roleIdList=this.systemRolePermissionService.list(Wrappers.<SystemRolePermissionDO>lambdaQuery().eq(SystemRolePermissionDO::getPermissionId,systemPermissionDO.getId())).stream().map(SystemRolePermissionDO::getRoleId).collect(Collectors.toList());
                List<Long> userIdList=this.systemUserRoleService.list(Wrappers.<SystemUserRoleDO>lambdaQuery().in(roleIdList.size()>0,SystemUserRoleDO::getRoleId,roleIdList)).stream().map(SystemUserRoleDO::getUserId).collect(Collectors.toList());
                for (Long userId:userIdList){
                    String username=this.systemUserService.getNameById(userId);
                    this.redisUtil.keyDelete(SystemRedisConstant.LOGIN_JWTUSER+SystemRedisConstant.SPLIT+username);
                }
            }

            return flag;
        }
    }

    public IPage<SystemPermissionDO> searchPage(SystemPermissionSearchDTO systemPermissionSearchDTO) {
        Long pageNum = Optional.ofNullable(systemPermissionSearchDTO.getPageNum()).orElse(0L);
        Long pageSize = Optional.ofNullable(systemPermissionSearchDTO.getPageSize()).orElse(-1L);

        IPage<SystemPermissionDO> systemPermissionDOIPage = (super.baseMapper).searchPage(new Page<>(pageNum, pageSize), systemPermissionSearchDTO);
        return systemPermissionDOIPage;
    }

    @Override
    public List<String> getPermissionByUserId(Long userId) {
        return super.baseMapper.getPermissionByUserId(userId);
    }

    @Override
    public String getNameById(Long permissionId) {
        return super.baseMapper.getNameById(permissionId);
    }
}
