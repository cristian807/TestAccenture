package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetBranchesByFranchiseUseCase {
    private final BranchRepository branchRepository;

    public GetBranchesByFranchiseUseCase(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public List<Branch> execute(Long franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId);
    }
}
