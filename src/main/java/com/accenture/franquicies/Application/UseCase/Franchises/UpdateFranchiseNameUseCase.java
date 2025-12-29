package com.accenture.franquicies.Application.UseCase.Franchises;

import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateFranchiseNameUseCase {

    private final FranchiseRepository franchiseRepository;

    public UpdateFranchiseNameUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<Franchise> execute(Long id, String newName) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("El ID de la franquicia no puede ser nulo"));
        }

        if (newName == null || newName.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la franquicia no puede estar vacío"));
        }

        return franchiseRepository.findById(id)
                .flatMap(existingFranchise -> {
                    Franchise updatedFranchise = Franchise.builder()
                            .id(existingFranchise.getId())
                            .name(newName.trim())
                            .build();
                    return franchiseRepository.save(updatedFranchise);
                });
    }
}
