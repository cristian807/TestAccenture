package com.accenture.franquicies.Domain.Repository;

import com.accenture.franquicies.Domain.Models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    List<Product> findAll();
    List<Product> findByBranchId(Long branchId);
    Optional<Product> findById(Long id);
    boolean deleteById(Long id);
    Optional<Product> updateStock(Long id, Integer stock);
    Optional<Product> updateName(Long id, String name);
    Optional<Product> findTopStockByBranchId(Long branchId);
}
