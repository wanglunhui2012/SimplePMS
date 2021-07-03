package indi.simple.pms.entity.datatransferobject;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:18
 * @Description:
 */
@Getter
@Setter
public class SystemChangePasswordDTO  implements Serializable {

    private static final long serialVersionUID = 438418123469207282L;

    @NotBlank(message = "不能为null!")
    @Size(max = 255, message = "最长255位!")
    private String oldPassword;

    @NotBlank(message = "不能为null!")
    @Size(max = 255, message = "最长255位!")
    private String newPassword;

    @NotBlank(message = "不能为null!")
    @Size(max = 255, message = "最长255位!")
    private String againNewPassword;

}

