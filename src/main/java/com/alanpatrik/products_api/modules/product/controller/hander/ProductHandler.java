package com.alanpatrik.products_api.modules.product.controller.hander;

import com.alanpatrik.products_api.modules.product.Product;
import com.alanpatrik.products_api.modules.product.ProductServiceImpl;
import com.alanpatrik.products_api.modules.validators.ProductValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductServiceImpl service;
    private final Validator validator = new ProductValidator();


    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getAll(), Product.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.getById(id), Product.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        final Mono<Product> productDTOMono = request.bodyToMono(Product.class).doOnNext(this::validate);
        return ServerResponse
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(productDTOMono.flatMap(service::create), Product.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        final Mono<Product> product = request.bodyToMono(Product.class).doOnNext(this::validate);
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.update(id, product), Product.class);
    }

    public Mono<ServerResponse> updateStorage(ServerRequest request) {
        String id = request.pathVariable("id");
        final Mono<Product> product = request.bodyToMono(Product.class).doOnNext(this::validate);
        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.updateStorage(id, product), Product.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ServerResponse
                .status(HttpStatus.NO_CONTENT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.delete(id), Product.class);
    }

    private void validate(Product product) {
        Errors errors = new BeanPropertyBindingResult(product, "product");
        validator.validate(product, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}
