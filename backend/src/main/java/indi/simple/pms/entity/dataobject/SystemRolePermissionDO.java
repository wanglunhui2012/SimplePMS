package indi.simple.pms.entity.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 系统角色权限关联表 system_role_permission 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:29
 */
@Getter
@Setter
@TableName("system_role_permission")
public class SystemRolePermissionDO implements Serializable {

    private static final long serialVersionUID = -93482470280137382L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 自增id

    @TableField("role_id")
    private Long roleId; // 角色id

    @TableField("permission_id")
    private Long permissionId; // 权限id

    public interface Update extends javax.validation.groups.Default {
    }

}
