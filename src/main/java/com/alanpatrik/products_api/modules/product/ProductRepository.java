package com.alanpatrik.products_api.modules.product;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<Product, String> {
    Mono<Product> findByName(String name);
}
