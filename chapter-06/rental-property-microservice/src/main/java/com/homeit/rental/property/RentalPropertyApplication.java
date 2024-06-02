package com.homeit.rental.property;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure
		.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RentalPropertyApplication {
	public static void main(String[] args) {
		SpringApplication app =
			new SpringApplication(RentalPropertyApplication.class);

		app.setDefaultProperties(
			Map.of("spring.threads.virtual.enabled", "true"));

		app.run(args);
	}
}

