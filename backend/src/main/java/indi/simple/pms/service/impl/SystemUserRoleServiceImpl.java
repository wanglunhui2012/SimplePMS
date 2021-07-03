package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.entity.dataobject.SystemUserRoleDO;
import indi.simple.pms.mapper.SystemUserRoleMapper;
import indi.simple.pms.service.SystemUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 系统用户角色关联表 system_user_role 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:38
 */
@Service("systemUserRoleService")
public class SystemUserRoleServiceImpl extends ServiceImpl<SystemUserRoleMapper, SystemUserRoleDO> implements SystemUserRoleService {
}
