package com.accenture.franquicies.Domain.Repository;

import com.accenture.franquicies.Domain.Models.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveBranchRepository {
    Mono<Branch> save(Branch branch);
    Flux<Branch> findAll();
    Flux<Branch> findByFranchiseId(Long franchiseId);
    Mono<Branch> findById(Long id);
    Mono<Boolean> deleteById(Long id);
    Mono<Branch> updateName(Long id, String name);
    Mono<Boolean> existsById(Long id);
}
