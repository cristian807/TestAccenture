package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetByIdBranchUseCase {
    private final BranchRepository branchRepository;

    public GetByIdBranchUseCase(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public Optional<Branch> execute(Long id) {
        return branchRepository.findById(id);
    }
}
