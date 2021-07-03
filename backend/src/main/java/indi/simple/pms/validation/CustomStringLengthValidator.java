package indi.simple.pms.validation;

import indi.simple.pms.validation.constraints.CustomStringLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:42
 * @Description:
 */
public class CustomStringLengthValidator implements ConstraintValidator<CustomStringLength, String> {
    private String message;
    private boolean nullAble;
    private boolean emptyAble;
    private int min;
    private int max;

    public CustomStringLengthValidator() {
    }

    public void initialize(CustomStringLength arg0) {
        this.message = arg0.message();
        this.nullAble = arg0.nullAble();
        this.emptyAble = arg0.emptyAble();
        this.min = arg0.min();
        this.max = arg0.max();
    }

    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (str == null) {
            return this.nullAble;
        } else if (str.trim().length() == 0) {
            return this.emptyAble;
        } else {
            int length = str.trim().length();
            return length <= this.max && length >= this.min;
        }
    }
}
