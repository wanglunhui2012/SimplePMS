package indi.simple.pms.entity.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统日志表 system_log 实体数据对象
 *
 * @author wanglunhui
 * @since 2021-04-19 23:52:02
 */
@Getter
@Setter
@TableName("system_log")
public class SystemLogDO implements Serializable {

    private static final long serialVersionUID = 471025955018275901L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 自增id

    @TableField("name")
    private String name; // 名称

    @TableField("user_id")
    private Long userId; // 用户id

    @TableField("user_name")
    private String username; // 用户名

    @TableField("url")
    private String url; // url

    @TableField("method")
    private String method; // 方法

    @TableField("params")
    private String params; // 参数

    @TableField("time")
    private Long time; // 耗时,单位ms

    @TableField("ip")
    private String ip; // ip地址

    @TableField("log_type")
    private String logType; // 类型,0登录日志,1操作日志

    @TableField("exception")
    private String exception; // 异常

    @TableField("create_time")
    private LocalDateTime createTime; // 创建日期

    public interface Update extends javax.validation.groups.Default {
    }

}