package com.hxy.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ConfigServerApplication
 * @date 2019年07月01日 15:43:26
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class,args);
    }

}
