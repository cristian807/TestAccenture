package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateBranchUseCase {
    private final BranchRepository branchRepository;

    public CreateBranchUseCase(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public Branch execute(String name, Long franchiseId) {
        Branch branch = Branch.builder()
                .id(null)
                .name(name)
                .franchiseId(franchiseId)
                .build();
        return branchRepository.save(branch);
    }
}
