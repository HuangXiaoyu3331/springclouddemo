package com.hxy.user.client.vo.reqeust;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: LoginUserVo
 * @date 2019年08月26日 16:49:07
 */
@Data
public class LoginUserVo {

    @NotBlank
    private String username;
    @Pattern(regexp = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S+$", message = "密码不正确")
    private String password;

}
