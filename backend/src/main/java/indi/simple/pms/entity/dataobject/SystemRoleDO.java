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
 * 系统角色表 system_role 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 20:58:11
 */
@Getter
@Setter
@TableName("system_role")
@JsonIgnoreProperties(value = {"createTime"}, allowGetters = true, allowSetters = false)
public class SystemRoleDO implements Serializable {

    private static final long serialVersionUID = -62206809169387751L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "不能为null!", groups = {SystemUserDO.Update.class})
    private Long id; // 自增id

    @TableField("name")
    @NotBlank(message = "不能为null!")
    @Size(max = 255, message = "最长255位!")
    private String name; // 名称

    @TableField("data_scope")
    @NotNull(message = "不能为null!")
    private String dataScope; // 数据权限,0全部,1本部门,2本部门及以下部门,3仅本人,4自定义

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
    @NotNull(message = "不能为null!")
    private List<Long> menuIdList;
    @TableField(exist = false)
    @NotNull(message = "不能为null!")
    private List<Long> permissionIdList;
    @TableField(exist = false)
    @NotNull(message = "不能为null!")
    private List<Long> departmentIdList;

    public interface Update extends javax.validation.groups.Default {
    }

}
