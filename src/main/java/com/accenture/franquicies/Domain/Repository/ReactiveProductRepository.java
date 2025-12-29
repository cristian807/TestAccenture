package com.accenture.franquicies.Domain.Repository;

import com.accenture.franquicies.Domain.Models.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveProductRepository {
    Mono<Product> save(Product product);
    Flux<Product> findAll();
    Flux<Product> findByBranchId(Long branchId);
    Mono<Product> findById(Long id);
    Mono<Boolean> deleteById(Long id);
    Mono<Product> updateStock(Long id, Integer stock);
    Mono<Product> updateName(Long id, String name);
    Mono<Product> findTopStockByBranchId(Long branchId);
    Mono<Boolean> existsById(Long id);
}
