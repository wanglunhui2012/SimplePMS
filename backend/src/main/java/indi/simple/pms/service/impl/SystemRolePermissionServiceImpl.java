package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.entity.dataobject.SystemRolePermissionDO;
import indi.simple.pms.mapper.SystemRolePermissionMapper;
import indi.simple.pms.service.SystemRolePermissionService;
import org.springframework.stereotype.Service;

/**
 * 系统角色权限关联表 system_role_permission 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:30
 */
@Service("systemRolePermissionService")
public class SystemRolePermissionServiceImpl extends ServiceImpl<SystemRolePermissionMapper, SystemRolePermissionDO> implements SystemRolePermissionService {
}
