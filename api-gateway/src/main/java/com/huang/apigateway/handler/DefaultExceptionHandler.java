package com.huang.apigateway.handler;

import com.hxy.common.core.ApiResponse;
import com.hxy.common.error.SystemError;
import com.hxy.common.exception.AppException;
import com.hxy.common.utils.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 错误异常处理
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: DefaultExceptionHandler
 * @date 2019年07月02日 09:59:18
 */
@Slf4j
@SuppressWarnings("all")
public class DefaultExceptionHandler implements ErrorWebExceptionHandler {

    /**
     * MessageReader
     */
    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

    /**
     * MessageWriter
     */
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

    /**
     * ViewResolvers
     */
    private List<ViewResolver> viewResolvers = Collections.emptyList();

    /**
     * 存储处理异常后的信息
     */
    private ThreadLocal<Map<String, Object>> exceptionHandlerResult = new ThreadLocal<>();

    /**
     * 参考 AbstractErrorWebExceptionHandler
     */
    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }

    /**
     * 参考 AbstractErrorWebExceptionHandler
     */
    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        Map<String, Object> result = new HashMap<>(2, 1);
        result.put("httpStatus", HttpStatus.OK);
        if (ex instanceof AppException) {
            // 处理业务异常
            ApiResponse response = ApiResponse.createByError(((AppException) ex).getBaseError());
            result.put("body", ObjectMapperUtil.obj2Json(response));
        } else if (ex instanceof NotFoundException) {
            ApiResponse response = ApiResponse.createByError(SystemError.SERVICE_NOT_FOUND);
            result.put("body", ObjectMapperUtil.obj2Json(response));
        } else {
            // 处理系统异常（其他异常）
            result.put("body", ObjectMapperUtil.obj2Json(ApiResponse.createByError(SystemError.SYSTEM_ERROR)));
        }
        exceptionHandlerResult.set(result);
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
                .switchIfEmpty(Mono.error(ex))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> write(exchange, response));

    }

    /**
     * 参考 AbstractErrorWebExceptionHandler
     */
    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

    /**
     * 参考 DefaultErrorWebExceptionHandler
     */
    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> result = exceptionHandlerResult.get();
        return ServerResponse.status((HttpStatus) result.get("httpStatus"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(result.get("body")));
    }

    /**
     * 参考 AbstractErrorWebExceptionHandler
     */
    private Mono<? extends Void> write(ServerWebExchange exchange,
                                       ServerResponse response) {
        exchange.getResponse().getHeaders()
                .setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }

    /**
     * 参考 AbstractErrorWebExceptionHandler
     */
    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return DefaultExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return DefaultExceptionHandler.this.viewResolvers;
        }

    }
}