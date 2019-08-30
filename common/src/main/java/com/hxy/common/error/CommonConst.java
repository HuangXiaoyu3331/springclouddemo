package com.hxy.common.error;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: CommonConst
 * @date 2019年08月29日 11:59:02
 */
public class CommonConst {

    public interface Cookie {
        /**
         * 登录cookie名称
         */
        String LOGIN_TOKEN = "login_token";
        /**
         * 用户登录过期时间,单位s
         */
        Integer REDIS_SESSION_EXPIRE_TIME = 60 * 60;
        /**
         * cookie path
         */
        String COOKIE_PATH = "/";
    }

}
