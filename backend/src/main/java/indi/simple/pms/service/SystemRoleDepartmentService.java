package indi.simple.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import indi.simple.pms.entity.dataobject.SystemRoleDepartmentDO;

import java.util.List;

/**
 * 系统角色部门关联表 system_role_department 服务类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:26
 */
public interface SystemRoleDepartmentService extends IService<SystemRoleDepartmentDO> {
    public List<Long> getDepartmentIdByRoleId(Long roleId);
}
