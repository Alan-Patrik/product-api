package com.alanpatrik.products_api.modules.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl {

    private final ProductRepository repository;

    public Flux<Product> getAll() {
        return repository.findAll();
    }

    public Mono<Product> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Product with id %s not found", id)))
                ).cast(Product.class);
    }

    private Mono<Product> getByName(String name) {
        return repository.findByName(name)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Product with name %s not found", name)))
                ).cast(Product.class);
    }

    public Mono<Product> create(Product product) {
        return repository.findByName(product.getName())
                .flatMap(p -> Mono.error(new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("Product with name %s has already been registered", p.getName())))
                ).switchIfEmpty(Mono.defer(() -> repository.save(product))).cast(Product.class);
    }

    public Mono<Product> update(String id, Mono<Product> product) {
        return getById(id)
                .flatMap(p -> product
                        .doOnNext(e -> e.setId(id)))
                .flatMap(repository::save);

    }

    public Mono<Product> updateStorage(String id, Mono<Product> product) {
        return getById(id)
                .flatMap(p -> product
                        .flatMap(e -> {
                            p.setId(id);
                            p.setQty(p.getQty() - e.getQty());
                            return Mono.just(p);
                        }))
                .flatMap(repository::save);
    }

    public Mono<Void> delete(String id) {
        return getById(id).flatMap(p -> repository.deleteById(id));
    }


//    public Flux<Product> updateStorage(Flux<Product> productFluxReceived) {
//        return productFluxReceived
//                .doOnNext(receivedProduct -> log.info("[INFO]: Updating Product [ {} ]", receivedProduct))
//                .flatMap(p -> updateQtyProducts(productFluxReceived))
//                .flatMap(repository::save)
//                .cast(Product.class);
//    }
//
//    private Flux<Product> updateQtyProducts(Flux<Product> productFluxReceived) {
//        return productFluxReceived
//                .flatMap(p -> getByName(p.getName()))
//                .flatMap(product -> productFluxReceived
//                .doOnNext(receivedProduct -> {
//                    product.setQty(product.getQty() - receivedProduct.getQty());
//                })).switchIfEmpty(p -> Mono.error(new ResponseStatusException(
//                                HttpStatus.BAD_REQUEST,
//                                "Unable to update stock")
//                        )
//                ).cast(Product.class);
//    }
}
