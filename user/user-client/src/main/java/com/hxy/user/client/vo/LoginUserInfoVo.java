package com.hxy.user.client.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: LoginUserInfoVo
 * @date 2019年08月27日 17:08:29
 */
@Data
public class LoginUserInfoVo {

    private Long id;

    private String userName;

    private String email;

    private String phone;

    private Date createTime;

    private Date updateTime;


}
