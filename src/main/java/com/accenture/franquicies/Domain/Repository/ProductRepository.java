package com.accenture.franquicies.Domain.Repository;

import com.accenture.franquicies.Domain.Models.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Product> findById(Long id);
    Flux<Product> findAll();
    Flux<Product> findByBranchId(Long branchId);
    Mono<Void> deleteById(Long id);
}
