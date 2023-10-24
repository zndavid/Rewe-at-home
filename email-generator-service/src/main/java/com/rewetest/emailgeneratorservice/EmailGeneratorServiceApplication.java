package com.rewetest.emailgeneratorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EmailGeneratorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailGeneratorServiceApplication.class, args);
    }
}
