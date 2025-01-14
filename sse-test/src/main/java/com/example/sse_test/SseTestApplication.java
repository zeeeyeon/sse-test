package com.example.sse_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SseTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SseTestApplication.class, args);
    }

}
