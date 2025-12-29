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
@DisplayName("CreateFranchiseUseCase Tests")
class CreateFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private CreateFranchiseUseCase createFranchiseUseCase;

    private Franchise testFranchise;

    @BeforeEach
    void setUp() {
        testFranchise = Franchise.builder()
                .id(1L)
                .name("Test Franchise")
                .build();
    }

    @Test
    @DisplayName("Debe crear una franquicia exitosamente")
    void shouldCreateFranchiseSuccessfully() {
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(testFranchise));

        StepVerifier.create(createFranchiseUseCase.execute("Test Franchise"))
                .expectNextMatches(franchise -> 
                    franchise.getId().equals(1L) && 
                    franchise.getName().equals("Test Franchise"))
                .verifyComplete();

        verify(franchiseRepository, times(1)).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el nombre es nulo")
    void shouldFailWhenNameIsNull() {
        StepVerifier.create(createFranchiseUseCase.execute(null))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("nombre de la franquicia es requerido"))
                .verify();

        verify(franchiseRepository, never()).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el nombre está vacío")
    void shouldFailWhenNameIsEmpty() {
        StepVerifier.create(createFranchiseUseCase.execute(""))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("nombre de la franquicia es requerido"))
                .verify();

        verify(franchiseRepository, never()).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Debe recortar espacios del nombre")
    void shouldTrimSpacesFromName() {
        when(franchiseRepository.save(any(Franchise.class))).thenReturn(Mono.just(testFranchise));

        StepVerifier.create(createFranchiseUseCase.execute("  Test Franchise  "))
                .expectNextMatches(franchise -> franchise.getName().equals("Test Franchise"))
                .verifyComplete();

        verify(franchiseRepository, times(1)).save(any(Franchise.class));
    }
}
