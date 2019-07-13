package com.huang.apigateway.controller;

import com.hxy.common.core.SystemError;
import com.hxy.common.exception.AppException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: WebFluxControllerTest
 * @date 2019年07月10日 14:11:59
 */
@RestController
public class WebFluxControllerTest {

    @GetMapping("/hello")
    public Mono<String> hello(){
        return Mono.just("Welcome to reactive world");
    }

    @GetMapping("/webfluxerror")
    public Mono<String> webfluxerror(){
        return Mono.error(new AppException(SystemError.SYSTEM_ERROR));
    }


}
