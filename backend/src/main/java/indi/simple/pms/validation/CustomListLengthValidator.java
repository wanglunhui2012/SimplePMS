package indi.simple.pms.validation;

import indi.simple.pms.validation.constraints.CustomListLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:42
 * @Description:
 */
public class CustomListLengthValidator implements ConstraintValidator<CustomListLength, List> {
    private String message;
    private boolean nullAble;
    private int min;
    private int max;

    public CustomListLengthValidator() {
    }

    public void initialize(CustomListLength arg0) {
        this.message = arg0.message();
        this.nullAble = arg0.nullAble();
        this.min = arg0.min();
        this.max = arg0.max();
    }

    public boolean isValid(List list, ConstraintValidatorContext constraintValidatorContext) {
        if (list == null) {
            return this.nullAble;
        } else {
            return list.size() >= this.min && list.size() <= this.max;
        }
    }
}
