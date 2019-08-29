package com.hxy.user.server.common;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: Const
 * @date 2019年08月26日 14:18:35
 */
public class Const {

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    /**
     * 用户登录过期时间,2个钟
     */
    public static final Integer REDIS_SESSION_EXPIRE_TIME = 60 * 60 * 2;

}
