package com.homeit.rental.property;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure
		.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
public class RentalPropertyApplication {
public static void main(String[] args) {
	SpringApplication app =
		new SpringApplication(RentalPropertyApplication.class);

	virtualThreads(app);
	databaseProperties(app);

	app.run(args);
}

	private static void virtualThreads(SpringApplication app) {
		app.setDefaultProperties(
			Map.of("spring.threads.virtual.enabled", "true"));
	}

	private static void databaseProperties(SpringApplication app) {
		// # H2 Database configuration in PostgreSQL mode
		app.setDefaultProperties(
			Map.of(
			"spring.datasource.url",
				"jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
			"spring.datasource.driverClassName",
				"org.h2.Driver",
			"spring.datasource.username",
				"sampleuser",
			"spring.datasource.password",
				"samplepass",
			"spring.jpa.database-platform",
				"org.hibernate.dialect.H2Dialect",
			"spring.h2.console.enabled",
				"true",
			"spring.h2.console.path",
				"/h2-console",
			"spring.jpa.hibernate.ddl-auto",
				"create",
			"spring.jpa.show-sql",
				"true"));
	}
}

