package indi.simple.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import indi.simple.pms.entity.dataobject.SystemRoleDepartmentDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统角色部门关联表 system_role_department 数据库映射类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:26
 */
public interface SystemRoleDepartmentMapper extends BaseMapper<SystemRoleDepartmentDO> {
    @Select("select department_id from system_role_department where role_id=#{roleId}")
    List<Long> getDepartmentIdByRoleId(@Param("roleId") Long roleId);
}
