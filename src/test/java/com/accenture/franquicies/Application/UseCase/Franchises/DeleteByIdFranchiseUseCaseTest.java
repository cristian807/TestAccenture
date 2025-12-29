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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteByIdFranchiseUseCase Tests")
class DeleteByIdFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private DeleteByIdFranchiseUseCase deleteByIdFranchiseUseCase;

    private Franchise testFranchise;

    @BeforeEach
    void setUp() {
        testFranchise = Franchise.builder()
                .id(1L)
                .name("Test Franchise")
                .build();
    }

    @Test
    @DisplayName("Debe eliminar una franquicia exitosamente")
    void shouldDeleteFranchiseSuccessfully() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(testFranchise));
        when(franchiseRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(deleteByIdFranchiseUseCase.execute(1L))
                .expectNext(true)
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(1L);
        verify(franchiseRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe retornar false cuando la franquicia no existe")
    void shouldReturnFalseWhenFranchiseNotFound() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(deleteByIdFranchiseUseCase.execute(1L))
                .expectNext(false)
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(1L);
        verify(franchiseRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Debe fallar cuando el ID es nulo")
    void shouldFailWhenIdIsNull() {
        StepVerifier.create(deleteByIdFranchiseUseCase.execute(null))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("ID de la franquicia no puede ser nulo"))
                .verify();

        verify(franchiseRepository, never()).findById(anyLong());
    }
}
