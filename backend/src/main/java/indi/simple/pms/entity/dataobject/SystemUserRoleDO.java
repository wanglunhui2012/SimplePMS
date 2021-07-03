package indi.simple.pms.entity.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 系统用户角色关联表 system_user_role 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:38
 */
@Getter
@Setter
@TableName("system_user_role")
public class SystemUserRoleDO implements Serializable {

    private static final long serialVersionUID = 325376988556176453L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 自增id

    @TableField("user_id")
    private Long userId; // 用户id

    @TableField("role_id")
    private Long roleId; // 角色id

    public interface Update extends javax.validation.groups.Default {
    }

}
