package com.accenture.franquicies.Domain.Repository;

import com.accenture.franquicies.Domain.Models.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(Long id);
    Flux<Franchise> findAll();
    Mono<Void> deleteById(Long id);
}
