package com.accenture.franquicies.Application.UseCase.Franchises;

import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
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
@DisplayName("GetAllFranchiseUseCase Tests")
class GetAllFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private GetAllFranchiseUseCase getAllFranchiseUseCase;

    @Test
    @DisplayName("Debe retornar todas las franquicias")
    void shouldReturnAllFranchises() {
        Franchise franchise1 = Franchise.builder().id(1L).name("Franchise 1").build();
        Franchise franchise2 = Franchise.builder().id(2L).name("Franchise 2").build();
        
        when(franchiseRepository.findAll()).thenReturn(Flux.just(franchise1, franchise2));

        StepVerifier.create(getAllFranchiseUseCase.execute())
                .expectNext(franchise1)
                .expectNext(franchise2)
                .verifyComplete();

        verify(franchiseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay franquicias")
    void shouldReturnEmptyWhenNoFranchises() {
        when(franchiseRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(getAllFranchiseUseCase.execute())
                .verifyComplete();

        verify(franchiseRepository, times(1)).findAll();
    }
}
