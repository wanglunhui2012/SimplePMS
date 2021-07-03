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

/**
 * 系统权限表 system_permission 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 21:07:05
 */
@Getter
@Setter
@TableName("system_permission")
@JsonIgnoreProperties(value = {"createTime"}, allowGetters = true, allowSetters = false)
public class SystemPermissionDO implements Serializable {

    private static final long serialVersionUID = -64717344882293585L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "不能为null!", groups = {Update.class})
    private Long id; // 自增id

    @TableField("name")
    @NotBlank(message = "不能为null!")
    @Size(max = 255, message = "最长255位!")
    private String name; // 名称

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

    public interface Update extends javax.validation.groups.Default {
    }

}