package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateBranchNameUseCase {
    private final BranchRepository branchRepository;

    public UpdateBranchNameUseCase(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public Optional<Branch> execute(Long id, String name) {
        return branchRepository.updateName(id, name);
    }
}
