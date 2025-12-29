package com.accenture.franquicies.Domain.Repository;

import com.accenture.franquicies.Domain.Models.Franchise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveFranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    Flux<Franchise> findAll();
    Mono<Franchise> findById(Long id);
    Mono<Boolean> deleteById(Long id);
    Mono<Franchise> updateName(Long id, String name);
    Mono<Boolean> existsById(Long id);
}
