package indi.simple.pms.validation;

import indi.simple.pms.validation.constraints.CustomStringRegexp;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:42
 * @Description:
 */
public class CustomStringRegexpValidator implements ConstraintValidator<CustomStringRegexp, String> {
    private String message;
    private boolean nullAble;
    private boolean emptyAble;
    private String regexp;

    public CustomStringRegexpValidator() {
    }

    public void initialize(CustomStringRegexp arg0) {
        this.message = arg0.message();
        this.nullAble = arg0.nullAble();
        this.emptyAble = arg0.emptyAble();
        this.regexp = arg0.regexp();
    }

    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (str == null) {
            return this.nullAble;
        } else if (str.trim().length() == 0) {
            return this.emptyAble;
        } else {
            return str.matches(this.regexp);
        }
    }
}
