package indi.simple.pms.entity.viewobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:21
 * @Description:
 */
@AllArgsConstructor
@Getter
@Setter
public class SystemTokenVO implements Serializable {
    private final String token;
}
