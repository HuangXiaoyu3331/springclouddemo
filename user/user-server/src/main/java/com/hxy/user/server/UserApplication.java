package com.hxy.user.server;

import com.hxy.common.annotation.EnableExceptionHandler;
import com.hxy.common.annotation.EnableRedisClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: UserApplication
 * @date 2019年08月26日 11:07:53
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableExceptionHandler
@EnableRedisClient
@MapperScan(basePackages = "com.hxy.user.server.dao")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }

}
