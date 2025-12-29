package com.accenture.franquicies.Infraestructure.Persistence.Repository;

import com.accenture.franquicies.Infraestructure.Persistence.Entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface JpaProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {
    Flux<ProductEntity> findByBranchId(Long branchId);
}
