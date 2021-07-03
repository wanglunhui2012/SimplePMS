package indi.simple.pms.entity.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 系统角色菜单关联表 system_role_menu 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:28
 */
@Getter
@Setter
@TableName("system_role_menu")
public class SystemRoleMenuDO implements Serializable {

    private static final long serialVersionUID = -58518829262958868L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 自增id

    @TableField("role_id")
    private Long roleId; // 角色id

    @TableField("menu_id")
    private Long menuId; // 菜单id

    public interface Update extends javax.validation.groups.Default {
    }

}
