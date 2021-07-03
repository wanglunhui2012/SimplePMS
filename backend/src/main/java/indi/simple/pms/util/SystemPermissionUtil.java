package indi.simple.pms.util;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.SystemRoleMenuDO;
import indi.simple.pms.service.SystemRoleMenuService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:40
 * @Description:
 */
public class SystemPermissionUtil {
    public SystemPermissionUtil() {
    }

    public static boolean isRoleBeyond(List<Long> roleIdList) {
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        return !jwtUserBO.getRoleIdList().containsAll(roleIdList);
    }

    public static boolean isMenuBeyond(List<Long> menuIdList) {
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();
        SystemRoleMenuService systemRoleMenuService=SpringUtil.getBean(SystemRoleMenuService.class);
        List<Long> myMenuIdList=systemRoleMenuService.list(Wrappers.<SystemRoleMenuDO>lambdaQuery().in(SystemRoleMenuDO::getRoleId, jwtUserBO.getRoleIdList())).stream().map(SystemRoleMenuDO::getMenuId).collect(Collectors.toList());
        return !myMenuIdList.containsAll(menuIdList);
    }
}

