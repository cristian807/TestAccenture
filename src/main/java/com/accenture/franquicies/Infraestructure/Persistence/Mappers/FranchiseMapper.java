package com.accenture.franquicies.Infraestructure.Persistence.Mappers;

import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.FranchiseEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FranchiseMapper {

    private final BranchMapper branchMapper;

    public FranchiseMapper(BranchMapper branchMapper) {
        this.branchMapper = branchMapper;
    }

    public Franchise toDomain(FranchiseEntity entity) {
        if (entity == null) return null;
        return Franchise.builder()
                .id(entity.getId())
                .name(entity.getName())
                .branches(entity.getBranches() != null ?
                        entity.getBranches().stream()
                                .map(branchMapper::toDomain)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    public Franchise toDomainSimple(FranchiseEntity entity) {
        if (entity == null) return null;
        return Franchise.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public FranchiseEntity toEntity(Franchise franchise) {
        if (franchise == null) return null;
        FranchiseEntity entity = new FranchiseEntity();
        entity.setId(franchise.getId());
        entity.setName(franchise.getName());
        return entity;
    }
}
