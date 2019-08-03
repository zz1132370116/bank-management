package com.zl.dc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by 舍頭襘游泳 on 2018/12/12.
 */
@SpringBootApplication          //Spring boot
@EnableZuulProxy                //网关
@EnableEurekaClient             //注册中心客户端
@EnableHystrix                  //熔断
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
