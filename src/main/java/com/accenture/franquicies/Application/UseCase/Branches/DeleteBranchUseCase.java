package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Repository.BranchRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteBranchUseCase {
    private final BranchRepository branchRepository;

    public DeleteBranchUseCase(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public boolean execute(Long id) {
        return branchRepository.deleteById(id);
    }
}
