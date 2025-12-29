package com.accenture.franquicies.Infraestructure.Persistence.Repository.Impl;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.ProductEntity;
import com.accenture.franquicies.Infraestructure.Persistence.Mappers.ProductMapper;
import com.accenture.franquicies.Infraestructure.Persistence.Repository.JpaProductRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final ProductMapper productMapper;

    public ProductRepositoryImpl(JpaProductRepository jpaProductRepository,
                                 ProductMapper productMapper) {
        this.jpaProductRepository = jpaProductRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Mono<Product> save(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        return jpaProductRepository.save(entity)
                .map(productMapper::toDomain);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return jpaProductRepository.findById(id)
                .map(productMapper::toDomain);
    }

    @Override
    public Flux<Product> findAll() {
        return jpaProductRepository.findAll()
                .map(productMapper::toDomain);
    }

    @Override
    public Flux<Product> findByBranchId(Long branchId) {
        return jpaProductRepository.findByBranchId(branchId)
                .map(productMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return jpaProductRepository.deleteById(id);
    }
}
