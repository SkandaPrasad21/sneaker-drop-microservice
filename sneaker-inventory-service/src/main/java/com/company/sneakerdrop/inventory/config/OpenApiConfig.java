package com.company.sneakerdrop.inventory.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Sneaker Inventory API", version = "1.0", description = "High-concurrency sneaker drop inventory service"))
public class OpenApiConfig {
	
}
