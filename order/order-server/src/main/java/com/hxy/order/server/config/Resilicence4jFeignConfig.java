package com.hxy.order.server.config;

import com.hxy.product.client.ProductClient;
import feign.Contract;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: Resilicence4jFeignConfig
 * @date 2019年08月05日 18:49:37
 */
@Configuration
public class Resilicence4jFeignConfig {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Bean
    public ProductClient productClient() {
        final URI uri = discoveryClient.getInstances("product").iterator().next().getUri();
        final CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("default");
        final FeignDecorators decorators = FeignDecorators.builder()
                .withCircuitBreaker(circuitBreaker)
                .withFallbackFactory(ProductClient.ProductClientFallback::new)
                .build();
        return Resilience4jFeign.builder(decorators)
                .contract(new SpringMvcContract())
                .target(ProductClient.class, uri.toString());
    }

}
