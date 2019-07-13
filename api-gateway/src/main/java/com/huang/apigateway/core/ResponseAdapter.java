package com.huang.apigateway.core;

import lombok.NonNull;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * response 适配器
 * 将Mono转成Flux
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ResponseAdapter
 * @date 2019年07月10日 18:33:19
 */
public class ResponseAdapter implements ClientHttpResponse {
    private final Flux<DataBuffer> flux;
    private final HttpHeaders headers;

    public ResponseAdapter(Publisher<? extends DataBuffer> body, HttpHeaders headers) {
        this.headers = headers;
        if (body instanceof Flux) {
            flux = (Flux<DataBuffer>) body;
        } else {
            flux = ((Mono) body).flux();
        }
    }

    @Override
    public @NonNull
    Flux<DataBuffer> getBody() {
        return flux;
    }

    @Override
    public @NonNull
    HttpHeaders getHeaders() {
        return headers;
    }

    @Override
    public @NonNull
    HttpStatus getStatusCode() {
        return null;
    }

    @Override
    public int getRawStatusCode() {
        return 0;
    }

    @Override
    public @NonNull
    MultiValueMap<String, ResponseCookie> getCookies() {
        return null;
    }
}
