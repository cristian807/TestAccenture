package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateBranchUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public CreateBranchUseCase(BranchRepository branchRepository, FranchiseRepository franchiseRepository) {
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<Branch> execute(String name, Long franchiseId) {
        if (name == null || name.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la sucursal es requerido"));
        }

        if (franchiseId == null) {
            return Mono.error(new IllegalArgumentException("El ID de la franquicia es requerido"));
        }

        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La franquicia con ID " + franchiseId + " no existe")))
                .flatMap(franchise -> {
                    Branch branch = Branch.builder()
                            .name(name.trim())
                            .franchiseId(franchiseId)
                            .build();
                    return branchRepository.save(branch);
                });
    }
}
