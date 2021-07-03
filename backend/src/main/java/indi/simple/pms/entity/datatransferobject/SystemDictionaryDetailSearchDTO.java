package indi.simple.pms.entity.datatransferobject;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 系统字典详情表 system_dictionary_detail 搜索DTO类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:15
 */
@Getter
@Setter
public class SystemDictionaryDetailSearchDTO implements Serializable {

    private static final long serialVersionUID = 208501228378394103L;

    private List<Long> idList; // 自增id列表

    private Long dictionaryId; // 字典id

    private String key; // 键

    private String value; // 值

    private String sqlSlot;

    private Long pageNum;// 页码

    private Long pageSize;// 页数

}
