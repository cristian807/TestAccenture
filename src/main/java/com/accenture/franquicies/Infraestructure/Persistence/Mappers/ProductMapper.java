package com.accenture.franquicies.Infraestructure.Persistence.Mappers;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toDomain(ProductEntity entity) {
        if (entity == null) return null;
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .stock(entity.getStock())
                .branchId(entity.getBranchId())
                .build();
    }

    public ProductEntity toEntity(Product product) {
        if (product == null) return null;
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();
    }
}
