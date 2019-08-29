package com.huang.apigateway.config;

import com.huang.apigateway.handler.DefaultExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * 异常处理类配置
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ExceptionConfig
 * @date 2019年07月11日 15:59:11
 */
@Configuration
public class ExceptionConfig {

    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                                             ServerCodecConfigurer serverCodecConfigurer) {
        DefaultExceptionHandler defaultExceptionHandler = new DefaultExceptionHandler();
        defaultExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        defaultExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        defaultExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return defaultExceptionHandler;
    }
}
