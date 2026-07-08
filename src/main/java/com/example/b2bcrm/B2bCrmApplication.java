package com.example.b2bcrm;

import com.example.b2bcrm.common.config.LocalPortCleaner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class B2bCrmApplication {

    public static void main(String[] args) {
        LocalPortCleaner.killProcessOnServerPort(args);
        SpringApplication.run(B2bCrmApplication.class, args);
    }
}
