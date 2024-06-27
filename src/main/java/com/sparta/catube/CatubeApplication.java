package com.sparta.catube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.sql.Time;

@EnableJpaAuditing
@SpringBootApplication
public class CatubeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatubeApplication.class, args);
    }

}
