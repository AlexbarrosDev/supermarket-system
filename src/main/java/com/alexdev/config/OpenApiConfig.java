package com.alexdev.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()

                .info(new Info()

                        .title("Project supermarket API")

                        .description("""
                                REST API developed with Spring Boot.
                                Responsible for user management.
                            """)
                        .version("v1.0.0")

                        .contact(new Contact()
                                .name("Alex Gateway")
                                .email("alexgateway@gmail.com")));
    }
}
