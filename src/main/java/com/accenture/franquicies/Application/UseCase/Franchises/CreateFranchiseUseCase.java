package com.accenture.franquicies.Application.UseCase.Franchises;

import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public CreateFranchiseUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<Franchise> execute(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la franquicia es requerido"));
        }

        Franchise franchise = Franchise.builder()
                .name(name.trim())
                .build();

        return franchiseRepository.save(franchise);
    }
}
