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
 * 系统字典详情表 system_dictionary_detail 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:14
 */
@Getter
@Setter
@TableName("system_dictionary_detail")
@JsonIgnoreProperties(value = {"createTime"}, allowGetters = true, allowSetters = false)
public class SystemDictionaryDetailDO implements Serializable {

    private static final long serialVersionUID = 364142382394833934L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "不能为空!", groups = {SystemDictionaryDetailDO.Update.class})
    private Long id; // 自增id

    @TableField("dictionary_id")
    @NotNull(message = "不能为空!")
    private Long dictionaryId; // 字典id

    @TableField("`key`")
    @NotNull(message = "不能为空!")
    private String key; // 键

    @TableField("value")
    @NotBlank(message = "不能为空!")
    @Size(max = 255, message = "最长255位!")
    private String value; // 值

    @TableField("sort")
    @NotNull(message = "不能为空!")
    private Integer sort; // 排序

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
