package com.huang.apigateway;

import com.hxy.common.annotation.EnableRedisClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ApiGatewayApplication
 * @date 2019年07月01日 12:07:46
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class,args);
    }
}
