package com.rush.logistic.client.company_product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CompanyProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyProductApplication.class, args);
	}

}
