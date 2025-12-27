package com.accenture.franquicies.Application.UseCase.Franchises;

import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteByIdFranchise {
    private final FranchiseRepository franchiseRepository;

    public DeleteByIdFranchise(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public boolean execute(Long id) {
        return franchiseRepository.deleteById(id);
    }
}
