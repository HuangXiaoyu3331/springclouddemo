package com.hxy.edge.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: EdgeApplication
 * @date 2019年08月08日 16:07:12
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EdgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdgeApplication.class,args);
    }

}
