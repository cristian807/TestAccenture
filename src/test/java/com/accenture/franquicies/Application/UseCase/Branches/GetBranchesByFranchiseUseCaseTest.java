package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetBranchesByFranchiseUseCase Tests")
class GetBranchesByFranchiseUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private GetBranchesByFranchiseUseCase getBranchesByFranchiseUseCase;

    @Test
    @DisplayName("Debe retornar sucursales por franquicia")
    void shouldReturnBranchesByFranchise() {
        Branch branch1 = Branch.builder().id(1L).name("Branch 1").franchiseId(1L).build();
        Branch branch2 = Branch.builder().id(2L).name("Branch 2").franchiseId(1L).build();
        
        when(branchRepository.findByFranchiseId(1L)).thenReturn(Flux.just(branch1, branch2));

        StepVerifier.create(getBranchesByFranchiseUseCase.execute(1L))
                .expectNext(branch1)
                .expectNext(branch2)
                .verifyComplete();

        verify(branchRepository, times(1)).findByFranchiseId(1L);
    }

    @Test
    @DisplayName("Debe fallar cuando el franchiseId es nulo")
    void shouldFailWhenFranchiseIdIsNull() {
        StepVerifier.create(getBranchesByFranchiseUseCase.execute(null))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("ID de la franquicia no puede ser nulo"))
                .verify();

        verify(branchRepository, never()).findByFranchiseId(anyLong());
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la franquicia no tiene sucursales")
    void shouldReturnEmptyWhenNoMatchingBranches() {
        when(branchRepository.findByFranchiseId(1L)).thenReturn(Flux.empty());

        StepVerifier.create(getBranchesByFranchiseUseCase.execute(1L))
                .verifyComplete();

        verify(branchRepository, times(1)).findByFranchiseId(1L);
    }
}
