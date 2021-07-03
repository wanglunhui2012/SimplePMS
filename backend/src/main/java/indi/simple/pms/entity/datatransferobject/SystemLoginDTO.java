package indi.simple.pms.entity.datatransferobject;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:19
 * @Description:
 */
@Getter
@Setter
public class SystemLoginDTO implements Serializable {
    private static final long serialVersionUID = -97512937016956804L;

    @NotBlank(message = "不能为null!")
    @Size(max = 255, message = "最长255位!")
    private String username;

    @NotBlank(message = "不能为null!")
    @Size(min = 32, max = 32, message = "必须为32位!")
    private String password;

    @NotBlank(message = "不能为null!")
    @Size(min = 32, max = 32, message = "必须为32位!")
    private String uuid;

    @NotBlank(message = "不能为null!")
    @Size(min = 4, max = 4, message = "必须为4位!")
    private String captcha;
}
