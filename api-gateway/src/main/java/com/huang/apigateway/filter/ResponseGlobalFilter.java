package com.huang.apigateway.filter;

import com.hxy.common.core.ApiResponse;
import com.huang.apigateway.core.ResponseAdapter;
import com.hxy.common.utils.JsonUtil;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.DefaultClientResponse;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.filter.NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;

/**
 * 响应封装过滤器，获取为服务返回信息，统一封装成ApiResponse返回到客户端
 * 参考类 ModifyResponseGatewayFilter
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ResponseGlobalFilter
 * @date 2019年07月10日 18:00:56
 */
//@Component
@SuppressWarnings("all")
public class ResponseGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取微服务response对象
        ServerHttpResponse originalResponse = exchange.getResponse();

        // 构建响应包装类
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(originalResponse) {
            /**
             * 使用给定的发布者，将消息主体写入下游http层
             * @param body 消息主体
             * @return Mono<Void>
             */
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                // 获取微服务响应 content-type
                String originalResponseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);

                HttpHeaders httpHeaders = new HttpHeaders();
                /*
                 explicitly add it in this way instead of 'httpHeaders.setContentType(originalResponseContentType)'
                 this will prevent exception in case of using non-standard media types like "Content-Type: image"
                */
                httpHeaders.add(HttpHeaders.CONTENT_TYPE, originalResponseContentType);
                ResponseAdapter responseAdapter = new ResponseAdapter(body, httpHeaders);

                DefaultClientResponse clientResponse = new DefaultClientResponse(responseAdapter,
                        ExchangeStrategies.withDefaults());

                // 修改响应体，将原本返回的响应体封装成 ApiResponse 返回
                Mono modifiedBody = clientResponse.bodyToMono(String.class)
                        .flatMap(originalBody -> Mono.just(JsonUtil.obj2String(ApiResponse.createBySuccess(originalBody))));

                BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);

                CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange,
                        exchange.getResponse().getHeaders());

                return bodyInserter.insert(outputMessage, new BodyInserterContext())
                        .then(Mono.defer(() -> {
                            Flux<DataBuffer> messageBody = outputMessage.getBody();
                            HttpHeaders headers = getDelegate().getHeaders();
                            if (!headers
                                    .containsKey(HttpHeaders.TRANSFER_ENCODING)) {
                                messageBody = messageBody
                                        .doOnNext(data -> headers.setContentLength(data.readableByteCount()));
                            }
                            return getDelegate().writeWith(messageBody);
                        }));
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body)
                        .flatMapSequential(p -> p));
            }
        };

        // 修改原本exchange的response，带入到下一个过滤链
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    @Override
    public int getOrder() {
        return WRITE_RESPONSE_FILTER_ORDER - 1;
    }
}
