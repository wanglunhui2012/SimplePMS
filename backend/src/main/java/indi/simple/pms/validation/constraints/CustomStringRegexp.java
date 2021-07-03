package indi.simple.pms.validation.constraints;

import indi.simple.pms.validation.CustomStringRegexpValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:42
 * @Description:
 */
@Constraint(
        validatedBy = {CustomStringRegexpValidator.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomStringRegexp {
    String message() default "Value is not match!";

    boolean nullAble() default true;

    boolean emptyAble() default true;

    String regexp() default ".*";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
