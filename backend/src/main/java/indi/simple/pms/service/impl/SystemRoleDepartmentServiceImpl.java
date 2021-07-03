package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.entity.dataobject.SystemRoleDepartmentDO;
import indi.simple.pms.mapper.SystemRoleDepartmentMapper;
import indi.simple.pms.service.SystemRoleDepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统角色部门关联表 system_role_department 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:26
 */
@Service("systemRoleDepartmentService")
public class SystemRoleDepartmentServiceImpl extends ServiceImpl<SystemRoleDepartmentMapper, SystemRoleDepartmentDO> implements SystemRoleDepartmentService {
    @Override
    public List<Long> getDepartmentIdByRoleId(Long roleId) {
        return super.baseMapper.getDepartmentIdByRoleId(roleId);
    }
}
