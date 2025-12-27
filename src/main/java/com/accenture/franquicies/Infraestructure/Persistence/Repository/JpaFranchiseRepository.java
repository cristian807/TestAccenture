package com.accenture.franquicies.Infraestructure.Persistence.Repository;

import com.accenture.franquicies.Infraestructure.Persistence.Entity.FranchiseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFranchiseRepository extends JpaRepository<FranchiseEntity, Long> {
}
