package com.hxy.common.utils;

import com.hxy.common.error.CommonConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie工具类
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: CookieUtil
 * @date 2019年08月26日 18:05:04
 */
@Slf4j
public class CookieUtil {

    /**
     * cookie 名称
     */
    private final static String COOKIE_NAME = CommonConst.User.LOGIN_TOKEN;
    /**
     * cookie 过期时间，单位s
     */
    private final static int MAX_AGE = CommonConst.User.REDIS_SESSION_EXPIRE_TIME;

    /**
     * 读取登录cookie
     */
    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                log.info("read cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    log.info("return cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 写入登录cookie到客户端
     */
    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie ck = new Cookie(COOKIE_NAME, token);
        //代表设置在根目录
        ck.setPath("/");
        ck.setHttpOnly(true);
        //单位是秒。
        //如果这个maxage不设置的话，cookie就不会写入硬盘，而是写在内存。只在当前页面有效。
        //如果是-1，代表永久
        ck.setMaxAge(MAX_AGE);
        response.addCookie(ck);
        log.info("write cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
    }


    /**
     * 删除客户端登录cookie
     */
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    ck.setPath("/");
                    //设置成0，代表删除此cookie。
                    ck.setMaxAge(0);
                    log.info("del cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }
}

