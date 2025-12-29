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
@DisplayName("GetAllBranchesUseCase Tests")
class GetAllBranchesUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private GetAllBranchesUseCase getAllBranchesUseCase;

    @Test
    @DisplayName("Debe retornar todas las sucursales")
    void shouldReturnAllBranches() {
        Branch branch1 = Branch.builder().id(1L).name("Branch 1").franchiseId(1L).build();
        Branch branch2 = Branch.builder().id(2L).name("Branch 2").franchiseId(1L).build();
        
        when(branchRepository.findAll()).thenReturn(Flux.just(branch1, branch2));

        StepVerifier.create(getAllBranchesUseCase.execute())
                .expectNext(branch1)
                .expectNext(branch2)
                .verifyComplete();

        verify(branchRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay sucursales")
    void shouldReturnEmptyWhenNoBranches() {
        when(branchRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(getAllBranchesUseCase.execute())
                .verifyComplete();

        verify(branchRepository, times(1)).findAll();
    }
}
