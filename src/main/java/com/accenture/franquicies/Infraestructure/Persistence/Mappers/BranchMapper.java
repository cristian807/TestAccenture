package com.accenture.franquicies.Infraestructure.Persistence.Mappers;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.BranchEntity;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.FranchiseEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BranchMapper {

    private final ProductMapper productMapper;

    public BranchMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Branch toDomain(BranchEntity entity) {
        if (entity == null) return null;
        return Branch.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchise() != null ? entity.getFranchise().getId() : null)
                .products(entity.getProducts() != null ?
                        entity.getProducts().stream()
                                .map(productMapper::toDomain)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    public Branch toDomainSimple(BranchEntity entity) {
        if (entity == null) return null;
        return Branch.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchise() != null ? entity.getFranchise().getId() : null)
                .build();
    }

    public BranchEntity toEntity(Branch branch, FranchiseEntity franchiseEntity) {
        if (branch == null) return null;
        BranchEntity entity = new BranchEntity();
        entity.setId(branch.getId());
        entity.setName(branch.getName());
        entity.setFranchise(franchiseEntity);
        return entity;
    }
}
