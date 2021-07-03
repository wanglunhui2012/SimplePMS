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
 * 系统用户表 system_user 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 20:52:20
 */
@Getter
@Setter
@TableName("system_user")
@JsonIgnoreProperties(value = {"avatar", "createTime"}, allowGetters = true, allowSetters = false)
public class SystemUserDO implements Serializable {

    private static final long serialVersionUID = -11604323923252330L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "不能为空!", groups = {SystemUserDO.Update.class})
    private Long id; // 自增id

    @TableField("name")
    @NotBlank(message = "不能为空!")
    @Size(max = 255, message = "最长255位!")
    private String name; // 名称

    @TableField("nick_name")
    @Size(max = 255, message = "最长255位!")
    private String nickName; // 昵称

    @TableField("password")
    @JsonIgnore
    private String password; // 密码

    @TableField("salt")
    @JsonIgnore
    private String salt; // 加密盐

    @TableField("avatar_url")
    private String avatarUrl; // 头像url

    @TableField("enable")
    private String enable; // 是否启用,0否,1是

    @TableField("email")
    @Email(message = "格式错误!")
    private String email; // 邮箱

    @TableField("phone_number")
    @Size(max = 255, message = "最长255位!")
    private String phoneNumber; // 联系电话

    @TableField("sex")
    @NotBlank(message = "不能为空!")
    private String sex; // 性别,0男,1女,2未知

    @TableField("remark")
    @Size(max = 255, message = "最长255位!")
    private String remark; // 备注

    @TableField("department_id")
    private Long departmentId; // 部门id

    @TableField("create_user_id")
    private Long createUserId; // 创建用户id

    @TableField("create_department_id")
    private Long createDepartmentId; // 创建部门id
    @TableField(exist = false)
    private String departmentName; // 创建部门name

    @TableField("create_time")
    private LocalDateTime createTime; // 创建日期

    @TableField("update_time")
    @JsonIgnore
    private LocalDateTime updateTime; // 修改日期

    @TableField(exist = false)
    @NotNull(message = "不能为null!")
    private List<Long> roleIdList;
    @TableField(exist = false)
    @NotNull(message = "不能为null!")
    private List<Long> jobIdList;

    public interface Update extends javax.validation.groups.Default {
    }

}
