package com.example.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @author:70968 Date:2021-10-23 11:21
 */
@EntityScan("com.example.cloud.*.entity")
@MapperScan("com.example.cloud.chat.mapper")
@SpringBootApplication
public class ChattingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChattingApplication.class, args);
    }
}
