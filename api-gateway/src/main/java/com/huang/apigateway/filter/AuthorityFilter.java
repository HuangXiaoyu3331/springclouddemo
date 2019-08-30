package com.huang.apigateway.filter;

import com.hxy.common.enums.UrlEnum;
import com.hxy.common.error.CommonConst;
import com.hxy.common.error.UserError;
import com.hxy.common.exception.AppException;
import com.hxy.common.utils.ObjectMapperUtil;
import com.hxy.common.utils.RedisUtil;
import com.hxy.user.client.vo.LoginUserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * 权限认证filter
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: AuthorityFilter
 * @date 2019年08月27日 16:18:41
 */
@Component
@Slf4j
@Order(HIGHEST_PRECEDENCE)
public class AuthorityFilter implements GlobalFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();
        log.info("用户请求接口:{}", requestPath);
        // 登录请求，不进行认证
        if (requestPath.equals(UrlEnum.LOGIN_PATH.getPath())) {
            return chain.filter(exchange);
        }

        // 其他接口需要认证
        List<HttpCookie> loginCookie = exchange.getRequest().getCookies().get(CommonConst.Cookie.LOGIN_TOKEN);
        if (CollectionUtils.isNotEmpty(loginCookie)) {
            String loginCookieValue = loginCookie.get(0).getValue();
            LoginUserInfoVo loginUserInfoVo = ObjectMapperUtil.str2Obj(redisUtil.get(loginCookieValue), LoginUserInfoVo.class);
            if (loginUserInfoVo != null) {
                // 用户已经登录
                // 重新设置redis session的过期时间
                redisUtil.expire(loginCookieValue, CommonConst.Cookie.REDIS_SESSION_EXPIRE_TIME);
                return chain.filter(exchange).then(
                        // .then方法执行的逻辑相当于zuul的后置过滤器
                        Mono.fromRunnable(() -> {
                            if (redisUtil.exists(CommonConst.Cookie.LOGIN_TOKEN)) {
                                // 重新设置cookie的过期时间
                                ResponseCookie responseCookie = ResponseCookie.from(CommonConst.Cookie.LOGIN_TOKEN, loginCookieValue)
                                        .maxAge(CommonConst.Cookie.REDIS_SESSION_EXPIRE_TIME)
                                        .httpOnly(true)
                                        .path(CommonConst.Cookie.COOKIE_PATH)
                                        .build();
                                exchange.getResponse().getCookies().add(CommonConst.Cookie.LOGIN_TOKEN, responseCookie);
                            }
                        })
                );
            }
        }

        // 认证不通过，抛出未登录异常
        throw new AppException(UserError.NOT_LOGGED_IN);
    }
}