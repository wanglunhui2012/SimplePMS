package indi.simple.pms.entity.datatransferobject;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 系统用户表 system_user 搜索DTO类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:31
 */
@Getter
@Setter
public class SystemUserSearchDTO implements Serializable {

    private static final long serialVersionUID = -43580540266790325L;

    private List<Long> idList; // 自增id列表

    private String name; // 名称

    private String nickName; // 昵称

    private String enable; // 是否启用,0否,1是

    private String email; // 邮箱

    private String phoneNumber; // 联系电话

    private String sex; // 性别,0男,1女

    private List<Long> departmentIdList; // 部门id列表

    private String sqlSlot;

    private Long pageNum;// 页码

    private Long pageSize;// 页数

}
