package com.company.sneakerdrop.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EnableJpaAuditing
@EnableFeignClients
public class SneakerOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SneakerOrderServiceApplication.class, args);
	}

}
