package indi.simple.pms.support.jsonmultipart;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:06
 * @Description:
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonMultipart {

    Class<?> classInfo();

}
