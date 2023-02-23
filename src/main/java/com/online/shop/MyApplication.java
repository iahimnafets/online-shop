package com.online.shop;

import com.online.shop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@Slf4j
public class MyApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService) {

        return args -> {
            log.info("Add some users into the system");
            userService.loadAllUsers();
        };
    }

}
