package com.LmsTest.Lab5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.LmsTest.Lab5.entity"})
@EnableJpaRepositories(basePackages = {"com.LmsTest.Lab5.repository"})
public class Lab5Application {
	public static void main(String[] args) {
		SpringApplication.run(Lab5Application.class, args);
	}
}
