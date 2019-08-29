package com.hxy.user.client.vo.reqeust;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: RegisterUserReqVo
 * @date 2019年08月26日 11:39:03
 */
@Data
public class RegisterUserReqVo {

    @NotBlank(message = "用户名不能为空")
    private String userName;
    @Pattern(regexp = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S+$", message = "必须包含字母、数字、符号至少2种")
    private String password;
    @Email(message = "邮箱格式不正确")
    private String email;
    @Pattern(regexp = "^1([34578])\\d{9}$",message = "请输入正确的手机号码")
    private String phone;

}
