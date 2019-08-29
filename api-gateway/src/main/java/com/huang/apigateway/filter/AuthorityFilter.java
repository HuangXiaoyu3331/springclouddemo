package com.huang.apigateway.filter;

import com.hxy.common.enums.UrlEnum;
import com.hxy.common.error.Const;
import com.hxy.common.error.SystemError;
import com.hxy.common.error.UserError;
import com.hxy.common.exception.AppException;
import com.hxy.common.utils.ObjectMapperUtil;
import com.hxy.common.utils.RedisUtil;
import com.hxy.user.client.vo.LoginUserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
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
        //登录请求，不进行认证
        if (requestPath.equals(UrlEnum.LOGIN_PATH.getPath())) {
            return chain.filter(exchange);
        }

        //其他接口需要认证
        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
        for (Map.Entry<String, List<HttpCookie>> entry : cookies.entrySet()) {
            String cookieName = entry.getValue().get(0).getName();
            String cookieValue = entry.getValue().get(0).getValue();
            log.info("cookieName:{},cookieValue:{}", cookieName, cookieValue);

            if (StringUtils.equals(Const.User.LOGIN_TOKEN, cookieName)) {
                LoginUserInfoVo loginUserInfoVo = ObjectMapperUtil.str2Obj(redisUtil.get(cookieValue), LoginUserInfoVo.class);
                if (loginUserInfoVo != null) {
                    // 认证通过
                    return chain.filter(exchange);
                }
            }
        }
        // 认证不通过，抛出未登录异常
        throw new AppException(UserError.NOT_LOGGED_IN);
    }
}