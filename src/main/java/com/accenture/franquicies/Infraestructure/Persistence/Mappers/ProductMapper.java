package com.accenture.franquicies.Infraestructure.Persistence.Mappers;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.BranchEntity;
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
                .branchId(entity.getBranch() != null ? entity.getBranch().getId() : null)
                .build();
    }

    public ProductEntity toEntity(Product product, BranchEntity branchEntity) {
        if (product == null) return null;
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setStock(product.getStock());
        entity.setBranch(branchEntity);
        return entity;
    }
}
