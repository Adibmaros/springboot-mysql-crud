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
                        .title("RESTful API CRUD Produk & User")
                        .version("1.1.0")
                        .description("Dokumentasi interaktif RESTful API CRUD Produk dan User dengan relasi One-to-Many menggunakan Spring Boot, Spring Data JPA, dan MySQL.")
                        .contact(new Contact()
                                .name("Adibmaros")
                                .url("https://github.com/Adibmaros/springboot-mysql-crud"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}
