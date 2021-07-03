package indi.simple.pms.entity.datatransferobject;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/19 21:55
 * @Description:
 */
@Getter
@Setter
public class SystemOnlineUserSearchDTO implements Serializable {

    private static final long serialVersionUID = 320754454012346528L;

    private Long pageNum;// 页码

    private Long pageSize;// 页数

}
