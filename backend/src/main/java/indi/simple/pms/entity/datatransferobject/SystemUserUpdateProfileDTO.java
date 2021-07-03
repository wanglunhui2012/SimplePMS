package indi.simple.pms.entity.datatransferobject;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Author: wanglunhui
 * @Date: 2021/6/14 19:19
 * @Description: 修改用户 Profile DTO
 */
@Getter
@Setter
public class SystemUserUpdateProfileDTO {
    @NotBlank(message = "不能为空!")
    @Size(max = 255, message = "最长255位!")
    private String name; // 名称

    @Size(max = 255, message = "最长255位!")
    private String nickName; // 昵称

    @Email(message = "格式错误!")
    private String email; // 邮箱

    @Size(max = 255, message = "最长255位!")
    private String phoneNumber; // 联系电话

    @NotBlank(message = "不能为空!")
    private String sex; // 性别,0男,1女,2未知

    @Size(max = 255, message = "最长255位!")
    private String remark; // 备注
}
