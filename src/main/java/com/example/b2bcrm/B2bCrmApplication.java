package com.example.b2bcrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class B2bCrmApplication {

    public static void main(String[] args) {
        LocalPortCleaner.killProcessOnServerPort(args);
        SpringApplication.run(B2bCrmApplication.class, args);
    }
}
