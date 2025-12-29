package com.accenture.franquicies.Application.UseCase.Franchises;

import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetByIdFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public GetByIdFranchiseUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<Franchise> execute(Long id) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("El ID de la franquicia no puede ser nulo"));
        }

        return franchiseRepository.findById(id);
    }
}
