package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Repository.BranchRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteBranchUseCase {

    private final BranchRepository branchRepository;

    public DeleteBranchUseCase(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public Mono<Boolean> execute(Long id) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("El ID de la sucursal no puede ser nulo"));
        }

        return branchRepository.findById(id)
                .flatMap(branch -> branchRepository.deleteById(id).thenReturn(true))
                .defaultIfEmpty(false);
    }
}
