package com.deity.liveroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.deity.liveroom.dao")
public class LiveroomApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveroomApplication.class, args);
    }
}
