package indi.simple.pms.entity.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统菜单表 system_menu 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 21:02:11
 */
@Getter
@Setter
@TableName(value = "system_menu",autoResultMap = true)
@JsonIgnoreProperties(value = {"createTime"}, allowGetters = true, allowSetters = false)
public class SystemMenuDO implements Serializable {

    private static final long serialVersionUID = -54022003213651894L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "不能为null!", groups = {SystemUserDO.Update.class})
    private Long id; // 自增id

    @TableField("name")
    @NotBlank(message = "不能为null!")
    @Size(max = 255, message = "最长255位!")
    private String name; // 名称

    @TableField("parent_id")
    @NotNull(message = "不能为null!")
    private Long parentId; // 父级菜单id

    @TableField("sort")
    @NotNull(message = "不能为null!")
    private Integer sort; // 排序

    @TableField("path")
    @NotBlank(message = "不能为null!")
    @Size(max = 255, message = "最长255位!")
    private String path; // 路径

    @TableField("component")
    @Size(max = 255, message = "最长255位!")
    private String component; // 组件

    @TableField("icon")
    @Size(max = 255, message = "最长255位!")
    private String icon; // 图标

    @TableField("redirect")
    @Size(max = 255, message = "最长255位!")
    private String redirect; // 重定向地址

    @TableField("hidden")
    private String hidden; // 是否隐藏,0否,1是

    @TableField("always_show")
    private String alwaysShow; // 是否总是显示,0否,1是

    @TableField("no_cache")
    private String noCache; // 是否不缓存,0否,1是

    @TableField("breadcrumb")
    private String breadcrumb; // 是否在面包屑显示,0否,1是

    @TableField("affix")
    private String affix; // 是否固定,0否,1是

    @TableField("remark")
    @Size(max = 255, message = "最长255位!")
    private String remark; // 备注

    @TableField("create_user_id")
    private Long createUserId; // 创建用户id

    @TableField("create_department_id")
    private Long createDepartmentId; // 创建部门id

    @TableField("create_time")
    private LocalDateTime createTime; // 创建日期

    @TableField("update_time")
    @JsonIgnore
    private LocalDateTime updateTime; // 修改日期

    @TableField(exist = false)
    private List<SystemMenuDO> children;

    public interface Update extends javax.validation.groups.Default {
    }

}
