package com.company.sneakerdrop.orderservice.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Sneaker Order API", version = "1.0", description = "High-concurrency sneaker drop order service"))
public class OpenApiConfig {

}
