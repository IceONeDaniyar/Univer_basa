package com.example.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;




@SpringBootApplication(scanBasePackages = "com.example.university")
@EntityScan(basePackages = "com.example.university.model") 
@EnableJpaRepositories(basePackages = "com.example.university.repository")
public class UniversityApp {
    public static void main(String[] args) {
        SpringApplication.run(UniversityApp.class, args);
    }
}
