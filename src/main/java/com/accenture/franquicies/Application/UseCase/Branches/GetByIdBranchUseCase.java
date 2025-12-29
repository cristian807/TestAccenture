package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetByIdBranchUseCase {

    private final BranchRepository branchRepository;

    public GetByIdBranchUseCase(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public Mono<Branch> execute(Long id) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("El ID de la sucursal no puede ser nulo"));
        }

        return branchRepository.findById(id);
    }
}
