package indi.simple.pms.aop.log;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:20
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    String value() default "";

    boolean isLogin() default false;

}
