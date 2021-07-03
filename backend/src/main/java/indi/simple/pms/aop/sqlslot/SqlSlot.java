package indi.simple.pms.aop.sqlslot;

import java.lang.annotation.*;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:33
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlSlot {
    /**
     * @return the Spring-EL expression to be evaluated before invoking the protected method
     */
    String value();
}
