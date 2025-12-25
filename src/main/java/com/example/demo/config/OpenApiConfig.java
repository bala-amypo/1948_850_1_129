package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // Basic OpenAPI info
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Bundle Save API")
                        .version("1.0")
                        .description("API documentation for Bundle Save project"));
    }

    // Scan all controllers in base package
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("ecommerce")
                .packagesToScan("com.example.demo.controller")
                .pathsToMatch("/**")
                .build();
    }
}
