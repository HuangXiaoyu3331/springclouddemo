package com.hxy.order.server;

import com.hxy.common.annotation.EnableExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: OrderApplication
 * @date 2019年07月01日 14:02:33
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableExceptionHandler
@EnableFeignClients(basePackages = {"com.hxy.product.client", "com.hxy.edge.client"})
//@EnableFeignClients(basePackages = {"com.hxy.order.server.remote", "com.hxy.edge.client"})
@ComponentScan(basePackages = {"com.hxy.product.client", "com.hxy.edge.client","com.hxy.order.server"})
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}