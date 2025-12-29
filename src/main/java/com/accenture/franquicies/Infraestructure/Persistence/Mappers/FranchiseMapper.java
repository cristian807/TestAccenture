package com.accenture.franquicies.Infraestructure.Persistence.Mappers;

import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.FranchiseEntity;
import org.springframework.stereotype.Component;

@Component
public class FranchiseMapper {

    public Franchise toDomain(FranchiseEntity entity) {
        if (entity == null) return null;
        return Franchise.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public FranchiseEntity toEntity(Franchise franchise) {
        if (franchise == null) return null;
        return FranchiseEntity.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }
}
