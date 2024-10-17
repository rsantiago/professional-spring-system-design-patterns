package com.homeit.authprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthproviderApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthproviderApplication.class, args);
	}

}
