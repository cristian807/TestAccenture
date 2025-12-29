package com.accenture.franquicies.Infraestructure.Persistence.Mappers;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.BranchEntity;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {

    public Branch toDomain(BranchEntity entity) {
        if (entity == null) return null;
        return Branch.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchiseId())
                .build();
    }

    public BranchEntity toEntity(Branch branch) {
        if (branch == null) return null;
        return BranchEntity.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }
}
