package com.huang.apigateway.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.function.Supplier;
import com.hxy.common.core.ApiResponse;
import com.hxy.common.error.SystemError;
import com.hxy.common.utils.ObjectMapperUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 限流异常捕获，返回json格式
 * 参考该类：SentinelGatewayBlockExceptionHandler
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: JsonSentinelGatewayBlockExceptionHandler
 * @date 2019年08月19日 15:10:19
 */
public class JsonSentinelGatewayBlockExceptionHandler implements WebExceptionHandler {
    private List<ViewResolver> viewResolvers;
    private List<HttpMessageWriter<?>> messageWriters;

    public JsonSentinelGatewayBlockExceptionHandler(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolvers;
        this.messageWriters = serverCodecConfigurer.getWriters();
    }

    /**
     * 重写返回值
     *
     * @param response
     * @param exchange
     * @return
     */
    private Mono<Void> writeResponse(ServerResponse response, ServerWebExchange exchange) {
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        byte[] datas = ObjectMapperUtil.obj2Json(ApiResponse.createByError(SystemError.CURRENT_LIMITING)).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(datas);
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        // This exception handler only handles rejection by Sentinel.
        if (!BlockException.isBlockException(ex)) {
            return Mono.error(ex);
        }
        return handleBlockedRequest(exchange, ex)
                .flatMap(response -> writeResponse(response, exchange));
    }

    private Mono<ServerResponse> handleBlockedRequest(ServerWebExchange exchange, Throwable throwable) {
        return GatewayCallbackManager.getBlockHandler().handleRequest(exchange, throwable);
    }

    private final Supplier<ServerResponse.Context> contextSupplier = () -> new ServerResponse.Context() {
        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return JsonSentinelGatewayBlockExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return JsonSentinelGatewayBlockExceptionHandler.this.viewResolvers;
        }
    };
}
