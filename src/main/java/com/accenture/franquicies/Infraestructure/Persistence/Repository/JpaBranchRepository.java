package com.accenture.franquicies.Infraestructure.Persistence.Repository;

import com.accenture.franquicies.Infraestructure.Persistence.Entity.BranchEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface JpaBranchRepository extends ReactiveCrudRepository<BranchEntity, Long> {
    Flux<BranchEntity> findByFranchiseId(Long franchiseId);
}
