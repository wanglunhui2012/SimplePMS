package indi.simple.pms.entity.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 系统用户职位关联表 system_user_job 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:37
 */
@Getter
@Setter
@TableName("system_user_job")
public class SystemUserJobDO implements Serializable {

    private static final long serialVersionUID = 581293708820761875L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 自增id

    @TableField("user_id")
    private Long userId; // 用户id

    @TableField("job_id")
    private Long jobId; // 职位id

    public interface Update extends javax.validation.groups.Default {
    }

}
