package indi.simple.pms.entity.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统部门表 system_department 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:07
 */
@Getter
@Setter
@TableName(value = "system_department",autoResultMap = true)
@JsonIgnoreProperties(value = {"createTime"}, allowGetters = true, allowSetters = false)
public class SystemDepartmentDO implements Serializable {

    private static final long serialVersionUID = 362289413695983419L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "不能为null!", groups = {SystemDepartmentDO.Update.class})
    private Long id; // 自增id

    @TableField("name")
    @NotBlank(message = "不能为null!")
    @Size(max = 255, message = "最长255位!")
    private String name; // 名称

    @TableField("parent_id")
    @NotNull(message = "不能为null!")
    private Long parentId; // 父级部门id

    @TableField("leader")
    @Size(max = 255, message = "最长255位!")
    private String leader; // 领导

    @TableField("phone_number")
    @Size(max = 255, message = "最长255位!")
    private String phoneNumber; // 联系电话

    @TableField("email")
    @Email(message = "格式不正确!")
    @Size(max = 255, message = "最长255位!")
    private String email; // 邮箱

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
    private List<SystemDepartmentDO> children;

    public interface Update extends javax.validation.groups.Default {
    }

}
