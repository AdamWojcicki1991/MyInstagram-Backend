package com.myinstagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MyInstagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyInstagramApplication.class, args);
    }

}
