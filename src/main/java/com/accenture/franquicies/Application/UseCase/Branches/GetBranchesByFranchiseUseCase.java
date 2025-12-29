package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GetBranchesByFranchiseUseCase {

    private final BranchRepository branchRepository;

    public GetBranchesByFranchiseUseCase(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public Flux<Branch> execute(Long franchiseId) {
        if (franchiseId == null) {
            return Flux.error(new IllegalArgumentException("El ID de la franquicia no puede ser nulo"));
        }

        return branchRepository.findByFranchiseId(franchiseId);
    }
}
