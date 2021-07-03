package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.entity.dataobject.SystemRoleMenuDO;
import indi.simple.pms.mapper.SystemRoleMenuMapper;
import indi.simple.pms.service.SystemRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 系统角色菜单关联表 system_role_menu 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:28
 */
@Service("systemRoleMenuService")
public class SystemRoleMenuServiceImpl extends ServiceImpl<SystemRoleMenuMapper, SystemRoleMenuDO> implements SystemRoleMenuService {
}
