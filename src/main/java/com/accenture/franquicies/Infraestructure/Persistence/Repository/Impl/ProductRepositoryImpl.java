package com.accenture.franquicies.Infraestructure.Persistence.Repository.Impl;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.BranchEntity;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.ProductEntity;
import com.accenture.franquicies.Infraestructure.Persistence.Mappers.ProductMapper;
import com.accenture.franquicies.Infraestructure.Persistence.Repository.JpaBranchRepository;
import com.accenture.franquicies.Infraestructure.Persistence.Repository.JpaProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final JpaBranchRepository jpaBranchRepository;
    private final ProductMapper productMapper;

    public ProductRepositoryImpl(JpaProductRepository jpaProductRepository,
                                 JpaBranchRepository jpaBranchRepository,
                                 ProductMapper productMapper) {
        this.jpaProductRepository = jpaProductRepository;
        this.jpaBranchRepository = jpaBranchRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product save(Product product) {
        BranchEntity branchEntity = jpaBranchRepository.findById(product.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + product.getBranchId()));
        
        ProductEntity entity = productMapper.toEntity(product, branchEntity);
        ProductEntity savedEntity = jpaProductRepository.save(entity);
        return productMapper.toDomain(savedEntity);
    }

    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll().stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByBranchId(Long branchId) {
        return jpaProductRepository.findByBranchId(branchId).stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaProductRepository.findById(id)
                .map(productMapper::toDomain);
    }

    @Override
    public boolean deleteById(Long id) {
        if (jpaProductRepository.existsById(id)) {
            jpaProductRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Product> updateStock(Long id, Integer stock) {
        return jpaProductRepository.findById(id)
                .map(entity -> {
                    entity.setStock(stock);
                    ProductEntity savedEntity = jpaProductRepository.save(entity);
                    return productMapper.toDomain(savedEntity);
                });
    }

    @Override
    public Optional<Product> updateName(Long id, String name) {
        return jpaProductRepository.findById(id)
                .map(entity -> {
                    entity.setName(name);
                    ProductEntity savedEntity = jpaProductRepository.save(entity);
                    return productMapper.toDomain(savedEntity);
                });
    }

    @Override
    public Optional<Product> findTopStockByBranchId(Long branchId) {
        return jpaProductRepository.findTopByBranchIdOrderByStockDesc(branchId)
                .map(productMapper::toDomain);
    }
}
