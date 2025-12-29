package com.accenture.franquicies.Application.UseCase.Branches;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
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
@DisplayName("UpdateBranchNameUseCase Tests")
class UpdateBranchNameUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private UpdateBranchNameUseCase updateBranchNameUseCase;

    private Branch existingBranch;
    private Branch updatedBranch;

    @BeforeEach
    void setUp() {
        existingBranch = Branch.builder()
                .id(1L)
                .name("Old Name")
                .franchiseId(1L)
                .build();
        
        updatedBranch = Branch.builder()
                .id(1L)
                .name("New Name")
                .franchiseId(1L)
                .build();
    }

    @Test
    @DisplayName("Debe actualizar el nombre de la sucursal exitosamente")
    void shouldUpdateBranchNameSuccessfully() {
        when(branchRepository.findById(1L)).thenReturn(Mono.just(existingBranch));
        when(branchRepository.save(any(Branch.class))).thenReturn(Mono.just(updatedBranch));

        StepVerifier.create(updateBranchNameUseCase.execute(1L, "New Name"))
                .expectNextMatches(branch -> branch.getName().equals("New Name"))
                .verifyComplete();

        verify(branchRepository, times(1)).findById(1L);
        verify(branchRepository, times(1)).save(any(Branch.class));
    }

    @Test
    @DisplayName("Debe retornar vacío cuando la sucursal no existe")
    void shouldReturnEmptyWhenBranchNotFound() {
        when(branchRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(updateBranchNameUseCase.execute(1L, "New Name"))
                .verifyComplete();

        verify(branchRepository, times(1)).findById(1L);
        verify(branchRepository, never()).save(any(Branch.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el ID es nulo")
    void shouldFailWhenIdIsNull() {
        StepVerifier.create(updateBranchNameUseCase.execute(null, "New Name"))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("ID de la sucursal no puede ser nulo"))
                .verify();

        verify(branchRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Debe fallar cuando el nombre es vacío")
    void shouldFailWhenNameIsEmpty() {
        StepVerifier.create(updateBranchNameUseCase.execute(1L, ""))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("nombre de la sucursal no puede estar vacío"))
                .verify();

        verify(branchRepository, never()).findById(anyLong());
    }
}
