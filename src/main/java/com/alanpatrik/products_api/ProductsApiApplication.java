package com.alanpatrik.products_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Products API",
        version = "v0.0.1",
        description = "Project created to be a microservice to create products",
        contact = @Contact(
                name = "Alan Patrik",
                email = "alanpatrik-fragozo@gmail.com",
                url = "https://github.com/Alan-Patrik"
        )))
public class ProductsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductsApiApplication.class, args);
    }
}
