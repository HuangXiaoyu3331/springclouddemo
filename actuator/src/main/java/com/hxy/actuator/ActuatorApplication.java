package com.hxy.actuator;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ActuatorApplication
 * @date 2019年07月16日 16:20:05
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class,args);
    }

}
