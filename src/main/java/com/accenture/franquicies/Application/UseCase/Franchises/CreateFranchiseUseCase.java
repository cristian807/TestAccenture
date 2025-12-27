package com.accenture.franquicies.Application.UseCase.Franchises;

import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateFranchiseUseCase {
    private final FranchiseRepository franchiseRepository;

    public CreateFranchiseUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Franchise execute(String name) {
        Franchise franchise = Franchise.builder()
                .id(null)
                .name(name)
                .build();
        return franchiseRepository.save(franchise);
    }
}
