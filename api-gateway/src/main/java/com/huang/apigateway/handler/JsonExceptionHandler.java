package com.huang.apigateway.handler;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: JsonExceptionHandler
 * @date 2019年07月02日 09:59:18
 */
//@Slf4j
//public class JsonExceptionHandler implements ErrorWebExceptionHandler {
//
//    @Setter
//    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();
//    @Setter
//    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();
//    @Setter
//    private List<ViewResolver> viewResolvers = Collections.emptyList();
//    private ThreadLocal<ApiResponse> exceptionHandlerResult = new ThreadLocal<>();
//
//    @Override
//    public Mono<Void> handle(ServerWebExchange exchange, Throwable exception) {
//        HttpStatus httpStatus;
//        String body;
//        /**
//         * 封装响应体,此body可修改为自己的jsonBody
//         */
////        if (exception instanceof AppException) {
//            ApiResponse response = ApiResponse.createByError(((AppException) exception).getBaseError());
//            ServerHttpRequest request = exchange.getRequest();
//            log.error("[全局异常处理]异常请求路径:{},记录异常信息:{}", request.getPath(), exception.getMessage());
//            if (exchange.getResponse().isCommitted()) {
//                return Mono.error(exception);
//            }
//            exceptionHandlerResult.set(response);
//            ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
//            return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)
//                    .route(newRequest)
//                    .switchIfEmpty(Mono.error(exception))
//                    .flatMap((handler) -> handler.handle(newRequest))
//                    .flatMap((res) -> write(exchange, res));
////        }
//
//
//        /**
//         * 错误记录
//         */
////        ServerHttpRequest request = exchange.getRequest();
////        log.error("[全局异常处理]异常请求路径:{},记录异常信息:{}", request.getPath(), ex.getMessage());
////        /**
////         * 参考AbstractErrorWebExceptionHandler
////         */
////        if (exchange.getResponse().isCommitted()) {
////            return Mono.error(exception);
////        }
////        exceptionHandlerResult.set(result);
////        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
////        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
////                .switchIfEmpty(Mono.error(ex))
////                .flatMap((handler) -> handler.handle(newRequest))
////                .flatMap((response) -> write(exchange, response));
//    }
//
//    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
//        ApiResponse response = exceptionHandlerResult.get();
//        return ServerResponse.status(response.getStatus())
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(BodyInserters.fromObject(response));
//    }
//
//    private Mono<? extends Void> write(ServerWebExchange exchange,
//                                       ServerResponse response) {
//        exchange.getResponse().getHeaders()
//                .setContentType(response.headers().getContentType());
//        return response.writeTo(exchange, new ResponseContext());
//    }
//
//    private class ResponseContext implements ServerResponse.Context {
//
//        @Override
//        public List<HttpMessageWriter<?>> messageWriters() {
//            return JsonExceptionHandler.this.messageWriters;
//        }
//
//        @Override
//        public List<ViewResolver> viewResolvers() {
//            return JsonExceptionHandler.this.viewResolvers;
//        }
//
//    }
//}
    @Slf4j
public class JsonExceptionHandler implements ErrorWebExceptionHandler {

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
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // 按照异常类型进行处理
        HttpStatus httpStatus;
        String body;
        if (ex instanceof NotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
            body = "Service Not Found";
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            httpStatus = responseStatusException.getStatus();
            body = responseStatusException.getMessage();
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            body = "Internal Server Error";
        }
        //封装响应体,此body可修改为自己的jsonBody
        Map<String, Object> result = new HashMap<>(2, 1);
        result.put("httpStatus", httpStatus);

        String msg = "{\"code\":" + httpStatus + ",\"message\": \"" + body + "\"}";
        result.put("body", msg);
        //错误记录
        ServerHttpRequest request = exchange.getRequest();
        log.error("[全局异常处理]异常请求路径:{},记录异常信息:{}", request.getPath(), ex.getMessage());
        //参考AbstractErrorWebExceptionHandler
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        exceptionHandlerResult.set(result);
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
                .switchIfEmpty(Mono.error(ex))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> write(exchange, response));

    }

    /**
     * 参考DefaultErrorWebExceptionHandler
     */
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> result = exceptionHandlerResult.get();
        return ServerResponse.status((HttpStatus) result.get("httpStatus"))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(result.get("body")));
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private Mono<? extends Void> write(ServerWebExchange exchange,
                                       ServerResponse response) {
        exchange.getResponse().getHeaders()
                .setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return JsonExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return JsonExceptionHandler.this.viewResolvers;
        }

    }
}