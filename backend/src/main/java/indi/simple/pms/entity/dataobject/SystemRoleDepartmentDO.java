package indi.simple.pms.entity.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 系统角色部门关联表 system_role_department 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:26
 */
@Getter
@Setter
@TableName("system_role_department")
public class SystemRoleDepartmentDO implements Serializable {

    private static final long serialVersionUID = 260431212720301166L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 自增id

    @TableField("role_id")
    private Long roleId; // 角色id

    @TableField("department_id")
    private Long departmentId; // 部门id

    public interface Update extends javax.validation.groups.Default {
    }

}
