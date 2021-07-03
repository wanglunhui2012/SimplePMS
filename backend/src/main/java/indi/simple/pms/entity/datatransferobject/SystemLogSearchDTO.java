package indi.simple.pms.entity.datatransferobject;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 系统日志表 system_log 搜索DTO类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:18
 */
@Getter
@Setter
public class SystemLogSearchDTO implements Serializable {

    private static final long serialVersionUID = -97501937016981804L;

    private String name; // 名称

    private String username; // 用户名

    private String logType; // 类型,0登录日志,1操作日志

    private Long pageNum;// 页码

    private Long pageSize;// 页数

}
