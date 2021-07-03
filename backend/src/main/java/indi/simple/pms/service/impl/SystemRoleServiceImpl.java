package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.constant.SystemRedisConstant;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.*;
import indi.simple.pms.entity.datatransferobject.SystemRoleSearchDTO;
import indi.simple.pms.exception.BadRequestException;
import indi.simple.pms.mapper.SystemRoleMapper;
import indi.simple.pms.service.*;
import indi.simple.pms.util.RedisUtil;
import indi.simple.pms.util.SecurityUtil;
import indi.simple.pms.util.SystemPermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 系统角色表 system_role 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:25
 */
@Service("systemRoleService")
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRoleDO> implements SystemRoleService {
    @Autowired
    private SystemRoleMenuServiceImpl systemRoleMenuService;
    @Autowired
    private SystemRolePermissionService systemRolePermissionService;
    @Autowired
    private SystemRoleDepartmentService systemRoleDepartmentService;
    @Autowired
    private SystemUserRoleService systemUserRoleService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean add(SystemRoleDO systemRoleDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        if (super.count(Wrappers.<SystemRoleDO>lambdaQuery().eq(SystemRoleDO::getName, systemRoleDO.getName())) > 0) {
            throw new BadRequestException("角色名已存在!");
        } else if (SystemPermissionUtil.isMenuBeyond(systemRoleDO.getMenuIdList())) {
            throw new BadRequestException("新增角色所选菜单,超越当前用户角色的菜单范围!");
        } else {
            systemRoleDO.setCreateUserId(jwtUserBO.getId());
            systemRoleDO.setCreateDepartmentId(jwtUserBO.getDepartmentId());
            systemRoleDO.setCreateTime(localDateTime);
            systemRoleDO.setUpdateTime(localDateTime);
            boolean flag = super.save(systemRoleDO);
            systemRoleDO.getMenuIdList().stream().forEach((menuId) -> {
                SystemRoleMenuDO systemRoleMenuDO = new SystemRoleMenuDO();
                systemRoleMenuDO.setRoleId(systemRoleDO.getId());
                systemRoleMenuDO.setMenuId(menuId);
                this.systemRoleMenuService.save(systemRoleMenuDO);
            });
            systemRoleDO.getPermissionIdList().stream().forEach((permissionId) -> {
                SystemRolePermissionDO systemRolePermissionDO = new SystemRolePermissionDO();
                systemRolePermissionDO.setRoleId(systemRoleDO.getId());
                systemRolePermissionDO.setPermissionId(permissionId);
                this.systemRolePermissionService.save(systemRolePermissionDO);
            });
            if("1".equals(systemRoleDO.getDataScope())){// 如果数据权限是自定义
                systemRoleDO.getDepartmentIdList().stream().forEach((departmentId) -> {
                    SystemRoleDepartmentDO systemRoleDepartmentDO = new SystemRoleDepartmentDO();
                    systemRoleDepartmentDO.setRoleId(systemRoleDO.getId());
                    systemRoleDepartmentDO.setDepartmentId(departmentId);
                    this.systemRoleDepartmentService.save(systemRoleDepartmentDO);
                });
            }

            return flag;
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean delete(List<Long> idList) {
        this.systemRoleMenuService.remove(Wrappers.<SystemRoleMenuDO>lambdaQuery().in(SystemRoleMenuDO::getRoleId, idList));
        this.systemRolePermissionService.remove(Wrappers.<SystemRolePermissionDO>lambdaQuery().in(SystemRolePermissionDO::getRoleId, idList));
        this.systemRoleDepartmentService.remove(Wrappers.<SystemRoleDepartmentDO>lambdaQuery().in(SystemRoleDepartmentDO::getRoleId, idList));

        boolean flag = super.removeByIds(idList);

        // 反向通过role_id获取user_id，再删除对应缓存
        List<SystemUserRoleDO> systemUserRoleDOList=this.systemUserRoleService.list(Wrappers.<SystemUserRoleDO>lambdaQuery().in(idList.size()>0,SystemUserRoleDO::getUserId,idList));
        for (SystemUserRoleDO systemUserRoleDO:systemUserRoleDOList){
            String username=this.systemUserService.getNameById(systemUserRoleDO.getUserId());
            this.redisUtil.keyDelete(SystemRedisConstant.LOGIN_JWTUSER+SystemRedisConstant.SPLIT+username);
        }

        return flag;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean update(SystemRoleDO systemRoleDO) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (super.count(Wrappers.<SystemRoleDO>lambdaQuery().eq(SystemRoleDO::getName, systemRoleDO.getName()).ne(SystemRoleDO::getId, systemRoleDO.getId())) > 0) {
            throw new BadRequestException("角色名已存在!");
        } else if (SystemPermissionUtil.isMenuBeyond(systemRoleDO.getMenuIdList())) {
            throw new BadRequestException("新增角色所选菜单,超越当前用户角色的菜单范围!");
        } else {
            this.systemRoleMenuService.remove(Wrappers.<SystemRoleMenuDO>lambdaQuery().eq(SystemRoleMenuDO::getRoleId, systemRoleDO.getId()));
            this.systemRolePermissionService.remove(Wrappers.<SystemRolePermissionDO>lambdaQuery().eq(SystemRolePermissionDO::getRoleId, systemRoleDO.getId()));
            this.systemRoleDepartmentService.remove(Wrappers.<SystemRoleDepartmentDO>lambdaQuery().eq(SystemRoleDepartmentDO::getRoleId, systemRoleDO.getId()));

            systemRoleDO.getMenuIdList().stream().forEach((menuId) -> {
                SystemRoleMenuDO systemRoleMenuDO = new SystemRoleMenuDO();
                systemRoleMenuDO.setRoleId(systemRoleDO.getId());
                systemRoleMenuDO.setMenuId(menuId);
                this.systemRoleMenuService.save(systemRoleMenuDO);
            });
            systemRoleDO.getPermissionIdList().stream().forEach((permissionId) -> {
                SystemRolePermissionDO systemRolePermissionDO = new SystemRolePermissionDO();
                systemRolePermissionDO.setRoleId(systemRoleDO.getId());
                systemRolePermissionDO.setPermissionId(permissionId);
                this.systemRolePermissionService.save(systemRolePermissionDO);
            });
            if("1".equals(systemRoleDO.getDataScope())){// 如果数据权限是自定义
                systemRoleDO.getDepartmentIdList().stream().forEach((departmentId) -> {
                    SystemRoleDepartmentDO systemRoleDepartmentDO = new SystemRoleDepartmentDO();
                    systemRoleDepartmentDO.setRoleId(systemRoleDO.getId());
                    systemRoleDepartmentDO.setDepartmentId(departmentId);
                    this.systemRoleDepartmentService.save(systemRoleDepartmentDO);
                });
            }
            systemRoleDO.setUpdateTime(localDateTime);
            boolean flag = super.updateById(systemRoleDO);

            // 修改角色，id不变，缓存中的JwtUser只有id，所以无需删除缓存

            return flag;
        }
    }

    public List<SystemRoleDO> getByUserId(Long userId) {
        return super.baseMapper.getByUserId(userId);
    }

    public IPage<SystemRoleDO> searchPage(SystemRoleSearchDTO systemRoleSearchDTO) {
        Long pageNum = Optional.ofNullable(systemRoleSearchDTO.getPageNum()).orElse(0L);
        Long pageSize = Optional.ofNullable(systemRoleSearchDTO.getPageSize()).orElse(-1L);
        IPage<SystemRoleDO> systemRoleDOIPage = super.baseMapper.searchPage(new Page<>(pageNum, pageSize), systemRoleSearchDTO);

        systemRoleDOIPage.getRecords().stream().forEach((systemRoleDO) -> {
            List<Long> menuIdList = this.systemRoleMenuService.list(Wrappers.<SystemRoleMenuDO>lambdaQuery().eq(SystemRoleMenuDO::getRoleId, systemRoleDO.getId())).stream().map(SystemRoleMenuDO::getMenuId).collect(Collectors.toList());
            systemRoleDO.setMenuIdList(menuIdList);
            List<Long> permissionIdList = this.systemRolePermissionService.list(Wrappers.<SystemRolePermissionDO>lambdaQuery().eq(SystemRolePermissionDO::getRoleId, systemRoleDO.getId())).stream().map(SystemRolePermissionDO::getPermissionId).collect(Collectors.toList());
            systemRoleDO.setPermissionIdList(permissionIdList);
            List<Long> departmentIdList = this.systemRoleDepartmentService.list(Wrappers.<SystemRoleDepartmentDO>lambdaQuery().eq(SystemRoleDepartmentDO::getRoleId, systemRoleDO.getId())).stream().map(SystemRoleDepartmentDO::getDepartmentId).collect(Collectors.toList());
            systemRoleDO.setDepartmentIdList(departmentIdList);
        });

        return systemRoleDOIPage;
    }

    @Override
    public List<Long> getCurrentRoleIdList() {
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();

        return this.getRoleIdListByUserId(jwtUserBO.getId());
    }

    @Override
    public List<Long> getRoleIdListByUserId(Long userId) {
        return super.baseMapper.getRoleIdListByUserId(userId);
    }
}
