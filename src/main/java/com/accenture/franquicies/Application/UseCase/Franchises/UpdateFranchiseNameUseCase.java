package com.accenture.franquicies.Application.UseCase.Franchises;

import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateFranchiseNameUseCase {
    private final FranchiseRepository franchiseRepository;

    public UpdateFranchiseNameUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Optional<Franchise> execute(Long id, String name) {
        return franchiseRepository.updateName(id, name);
    }
}
