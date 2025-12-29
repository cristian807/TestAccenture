package com.accenture.franquicies.Infraestructure.Persistence.Repository;

import com.accenture.franquicies.Infraestructure.Persistence.Entity.FranchiseEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFranchiseRepository extends ReactiveCrudRepository<FranchiseEntity, Long> {
}
