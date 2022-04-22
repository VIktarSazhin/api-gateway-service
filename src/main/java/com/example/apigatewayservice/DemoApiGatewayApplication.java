package com.example.apigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
@EnableDiscoveryClient
public class DemoApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApiGatewayApplication.class, args);
    }
}
