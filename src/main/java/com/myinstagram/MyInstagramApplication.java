package com.myinstagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MyInstagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyInstagramApplication.class, args);
    }

}
