package indi.simple.pms.entity.viewobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:20
 * @Description:
 */
@Getter
@Setter
public class SystemNavigationMenuVO {
    private String name;
    private String path;
    private String component;
    private String redirect;
    private Boolean hidden;
    private Boolean alwaysShow;
    private SystemNavigationMenuMetaVO meta;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<SystemNavigationMenuVO> children;
}
