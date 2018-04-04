package com.example.cmiss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CmissApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmissApplication.class, args);
	}
}
