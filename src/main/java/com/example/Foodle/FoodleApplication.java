package com.example.Foodle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// @SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class FoodleApplication {
	public static void main(String[] args) {
		SpringApplication.run(FoodleApplication.class, args);
	}

}
