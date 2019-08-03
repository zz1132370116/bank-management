package com.zl.dc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by 舍頭襘游泳 on 2018/12/13.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class AuthServiceApplication {
    public static void main(String[] args) {

    SpringApplication.run(AuthServiceApplication.class,args);
    }
}
