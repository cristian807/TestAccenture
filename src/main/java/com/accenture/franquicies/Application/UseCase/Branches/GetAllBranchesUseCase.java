package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GetAllBranchesUseCase {

    private final BranchRepository branchRepository;

    public GetAllBranchesUseCase(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public Flux<Branch> execute() {
        return branchRepository.findAll();
    }
}
