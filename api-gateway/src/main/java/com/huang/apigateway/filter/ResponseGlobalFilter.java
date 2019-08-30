package com.huang.apigateway.filter;

import com.hxy.common.error.CommonConst;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import static org.springframework.cloud.gateway.filter.NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;

/**
 * 响应封装过滤器
 * 参考类 ModifyResponseGatewayFilter
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ResponseGlobalFilter
 * @date 2019年07月10日 18:00:56
 */
@Component
@Order(WRITE_RESPONSE_FILTER_ORDER - 1)
@SuppressWarnings("all")
public class ResponseGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 用户访问重新设置cookie的过期时间
        String loginCookieValue = exchange.getRequest().getCookies().get(CommonConst.User.LOGIN_TOKEN).get(0).getValue();
        ResponseCookie responseCookie = ResponseCookie.from(CommonConst.User.LOGIN_TOKEN, loginCookieValue)
                .maxAge(CommonConst.User.REDIS_SESSION_EXPIRE_TIME)
                .httpOnly(true)
                .path("/")
                .build();
        exchange.getResponse().getCookies().add(CommonConst.User.LOGIN_TOKEN, responseCookie);
        return chain.filter(exchange);
    }
}
