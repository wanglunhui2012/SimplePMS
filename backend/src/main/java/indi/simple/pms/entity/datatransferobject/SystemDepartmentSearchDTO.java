package indi.simple.pms.entity.datatransferobject;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 系统部门表 system_department 搜索DTO类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:09
 */
@Getter
@Setter
public class SystemDepartmentSearchDTO implements Serializable {

    private static final long serialVersionUID = 438418805869207282L;

    private List<Long> idList; // 自增id列表

    private String name; // 名称

    private Long parentId; // 父级部门id

    private String leader; // 领导

    private String sqlSlot;

    private Long pageNum;// 页码

    private Long pageSize;// 页数

}
