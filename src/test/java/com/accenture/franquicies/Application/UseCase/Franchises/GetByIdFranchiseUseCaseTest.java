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
@DisplayName("GetByIdFranchiseUseCase Tests")
class GetByIdFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private GetByIdFranchiseUseCase getByIdFranchiseUseCase;

    private Franchise testFranchise;

    @BeforeEach
    void setUp() {
        testFranchise = Franchise.builder()
                .id(1L)
                .name("Test Franchise")
                .build();
    }

    @Test
    @DisplayName("Debe retornar la franquicia cuando existe")
    void shouldReturnFranchiseWhenExists() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.just(testFranchise));

        StepVerifier.create(getByIdFranchiseUseCase.execute(1L))
                .expectNext(testFranchise)
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar vacío cuando la franquicia no existe")
    void shouldReturnEmptyWhenFranchiseNotFound() {
        when(franchiseRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(getByIdFranchiseUseCase.execute(1L))
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe fallar cuando el ID es nulo")
    void shouldFailWhenIdIsNull() {
        StepVerifier.create(getByIdFranchiseUseCase.execute(null))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("ID de la franquicia no puede ser nulo"))
                .verify();

        verify(franchiseRepository, never()).findById(anyLong());
    }
}
