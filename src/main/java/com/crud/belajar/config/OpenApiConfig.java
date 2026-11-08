package com.crud.belajar.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RESTful API CRUD Produk")
                        .version("1.0.0")
                        .description("Dokumentasi interaktif RESTful API CRUD Produk menggunakan Spring Boot, Spring Data JPA, dan MySQL.")
                        .contact(new Contact()
                                .name("Developer")
                                .url("https://github.com/username-kamu/springboot-mysql-crud"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}

