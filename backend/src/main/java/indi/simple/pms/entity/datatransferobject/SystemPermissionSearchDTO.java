package indi.simple.pms.entity.datatransferobject;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 系统权限表 system_permission 搜索DTO类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:22
 */
@Getter
@Setter
public class SystemPermissionSearchDTO implements Serializable {

    private static final long serialVersionUID = 581398889512279475L;

    private List<Long> idList; // 自增id列表

    private String name; // 名称

    private String sqlSlot;

    private Long pageNum;// 页码

    private Long pageSize;// 页数

}
