package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateBranchUseCase Tests")
class CreateBranchUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private CreateBranchUseCase createBranchUseCase;

    private Franchise testFranchise;
    private Branch testBranch;

    @BeforeEach
    void setUp() {
        testFranchise = Franchise.builder()
                .id(1L)
                .name("Test Franchise")
                .build();
        
        testBranch = Branch.builder()
                .id(1L)
                .name("Test Branch")
                .franchiseId(1L)
                .build();
    }

    @Test
    @DisplayName("Debe crear una sucursal exitosamente")
    void shouldCreateBranchSuccessfully() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(testFranchise));
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(testBranch));

        StepVerifier.create(createBranchUseCase.execute("Test Branch", 1L))
                .expectNextMatches(branch -> 
                    branch.getId().equals(1L) && 
                    branch.getName().equals("Test Branch") &&
                    branch.getFranchiseId().equals(1L))
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(1L);
        verify(branchRepository, times(1)).save(any(Branch.class));
    }

    @Test
    @DisplayName("Debe fallar cuando la franquicia no existe")
    void shouldFailWhenFranchiseNotFound() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(createBranchUseCase.execute("Test Branch", 1L))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("no existe"))
                .verify();

        verify(branchRepository, never()).save(any(Branch.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el nombre es nulo")
    void shouldFailWhenNameIsNull() {
        StepVerifier.create(createBranchUseCase.execute(null, 1L))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("nombre de la sucursal es requerido"))
                .verify();

        verify(branchRepository, never()).save(any(Branch.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el franchiseId es nulo")
    void shouldFailWhenFranchiseIdIsNull() {
        StepVerifier.create(createBranchUseCase.execute("Test Branch", null))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("ID de la franquicia es requerido"))
                .verify();

        verify(branchRepository, never()).save(any(Branch.class));
    }
}
