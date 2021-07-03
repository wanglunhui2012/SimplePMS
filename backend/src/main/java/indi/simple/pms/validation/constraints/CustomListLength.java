package indi.simple.pms.validation.constraints;

import indi.simple.pms.validation.CustomListLengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:42
 * @Description:
 */
@Constraint(
        validatedBy = {CustomListLengthValidator.class}
)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomListLength {
    String message() default "Length is not match!";

    boolean nullAble() default true;

    int min() default 0;

    int max() default 2147483647;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
