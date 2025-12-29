package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateBranchNameUseCase {

    private final BranchRepository branchRepository;

    public UpdateBranchNameUseCase(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public Mono<Branch> execute(Long id, String newName) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("El ID de la sucursal no puede ser nulo"));
        }

        if (newName == null || newName.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la sucursal no puede estar vacío"));
        }

        return branchRepository.findById(id)
                .flatMap(existingBranch -> {
                    Branch updatedBranch = Branch.builder()
                            .id(existingBranch.getId())
                            .name(newName.trim())
                            .franchiseId(existingBranch.getFranchiseId())
                            .build();
                    return branchRepository.save(updatedBranch);
                });
    }
}
