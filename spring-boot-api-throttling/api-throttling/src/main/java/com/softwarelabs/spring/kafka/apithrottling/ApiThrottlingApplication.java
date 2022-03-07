package com.softwarelabs.spring.kafka.apithrottling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiThrottlingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiThrottlingApplication.class, args);
	}

}
