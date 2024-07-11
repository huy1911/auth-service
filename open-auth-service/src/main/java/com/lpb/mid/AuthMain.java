package com.lpb.mid;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class AuthMain {
    public static void main(String[] args) {
        SpringApplication.run(AuthMain.class, args);
    }
}