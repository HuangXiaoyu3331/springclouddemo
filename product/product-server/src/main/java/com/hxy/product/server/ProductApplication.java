package com.hxy.product.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductApplication
 * @date 2019年07月01日 13:53:34
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.hxy.product.server.dao")
@ComponentScan(basePackages = "com.hxy.common.config")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,args);
    }

}
