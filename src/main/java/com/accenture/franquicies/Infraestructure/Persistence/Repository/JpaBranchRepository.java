package com.accenture.franquicies.Infraestructure.Persistence.Repository;

import com.accenture.franquicies.Infraestructure.Persistence.Entity.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaBranchRepository extends JpaRepository<BranchEntity, Long> {
    List<BranchEntity> findByFranchiseId(Long franchiseId);
}
