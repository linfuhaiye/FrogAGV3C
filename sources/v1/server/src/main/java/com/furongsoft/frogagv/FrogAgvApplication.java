package com.furongsoft.frogagv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.furongsoft"})
@MapperScan("com.furongsoft.**.mappers")
public class FrogAgvApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrogAgvApplication.class, args);
    }
}
