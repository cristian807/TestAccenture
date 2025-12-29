package com.accenture.franquicies.Application.UseCase.Franchises;

import com.accenture.franquicies.Domain.Models.Franchise;
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
@DisplayName("UpdateFranchiseNameUseCase Tests")
class UpdateFranchiseNameUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    private Franchise existingFranchise;
    private Franchise updatedFranchise;

    @BeforeEach
    void setUp() {
        existingFranchise = Franchise.builder()
                .id(1L)
                .name("Old Name")
                .build();
        
        updatedFranchise = Franchise.builder()
                .id(1L)
                .name("New Name")
                .build();
    }

    @Test
    @DisplayName("Debe actualizar el nombre de la franquicia exitosamente")
    void shouldUpdateFranchiseNameSuccessfully() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(existingFranchise));
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(updatedFranchise));

        StepVerifier.create(updateFranchiseNameUseCase.execute(1L, "New Name"))
                .expectNextMatches(franchise -> franchise.getName().equals("New Name"))
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(1L);
        verify(franchiseRepository, times(1)).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Debe retornar vacío cuando la franquicia no existe")
    void shouldReturnEmptyWhenFranchiseNotFound() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(updateFranchiseNameUseCase.execute(1L, "New Name"))
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(1L);
        verify(franchiseRepository, never()).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el ID es nulo")
    void shouldFailWhenIdIsNull() {
        StepVerifier.create(updateFranchiseNameUseCase.execute(null, "New Name"))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("ID de la franquicia no puede ser nulo"))
                .verify();

        verify(franchiseRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Debe fallar cuando el nombre es vacío")
    void shouldFailWhenNameIsEmpty() {
        StepVerifier.create(updateFranchiseNameUseCase.execute(1L, ""))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("nombre de la franquicia no puede estar vacío"))
                .verify();

        verify(franchiseRepository, never()).findById(anyLong());
    }
}
