package com.accenture.franquicies.Infraestructure.Persistence.Repository;

import com.accenture.franquicies.Infraestructure.Persistence.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByBranchId(Long branchId);
    
    @Query("SELECT p FROM ProductEntity p WHERE p.branch.id = :branchId ORDER BY p.stock DESC LIMIT 1")
    Optional<ProductEntity> findTopByBranchIdOrderByStockDesc(@Param("branchId") Long branchId);
}
