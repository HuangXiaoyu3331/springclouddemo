package com.hxy.ekserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: EkServerApplication
 * @date 2019年07月01日 12:00:39
 */
@SpringBootApplication
@EnableEurekaServer
public class EkServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EkServerApplication.class,args);
    }

}
