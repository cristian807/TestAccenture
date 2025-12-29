package com.accenture.franquicies.Application.UseCase.Franchises;

import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteByIdFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public DeleteByIdFranchiseUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<Boolean> execute(Long id) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("El ID de la franquicia no puede ser nulo"));
        }

        return franchiseRepository.findById(id)
                .flatMap(franchise -> franchiseRepository.deleteById(id).thenReturn(true))
                .defaultIfEmpty(false);
    }
}
