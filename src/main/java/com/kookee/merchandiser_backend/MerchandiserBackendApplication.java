package com.kookee.merchandiser_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.kookee.merchandiser_backend")
public class MerchandiserBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MerchandiserBackendApplication.class, args);
	}
}