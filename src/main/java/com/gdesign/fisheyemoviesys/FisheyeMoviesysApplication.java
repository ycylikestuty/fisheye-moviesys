package com.gdesign.fisheyemoviesys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * @author ycy
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

@MapperScan("com.gdesign.fisheyemoviesys.mapper")
public class FisheyeMoviesysApplication {

    public static void main(String[] args) {
        SpringApplication.run(FisheyeMoviesysApplication.class, args);
    }

}
