package com.themarblefamily.marblecoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.themarblefamily.marblecoin.market", "com.themarblefamily.marblecoin.sim"})
public class MarbleCoinApp {
    public static void main(String[] args) {
        SpringApplication.run(MarbleCoinApp.class, args);
    }
}
