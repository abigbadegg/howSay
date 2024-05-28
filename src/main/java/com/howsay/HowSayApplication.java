package com.howsay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.darcytech.doris.alert.annotation.EnableAlert;

@SpringBootApplication
@EnableAlert
@EnableScheduling
public class HowSayApplication {

    public static void main(String[] args) {
        SpringApplication.run(HowSayApplication.class, args);
    }

}
