package com.hxy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * url 枚举
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: UrlEnum
 * @date 2019年08月29日 11:39:12
 */
@AllArgsConstructor
@Getter
public enum UrlEnum {

    /**
     * 登录
     */
    LOGIN_PATH("/user/login", "登录接口地址"),
    ;

    private String path;
    private String describe;

}
