package com.accenture.franquicies.Interfaceadapter.Controllers;

import com.accenture.franquicies.Application.UseCase.Franchises.*;
import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.CreateFranchiseRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.UpdateNameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FranchiseController Tests")
class FranchiseControllerTest {

    @Mock
    private CreateFranchiseUseCase createFranchiseUseCase;
    
    @Mock
    private GetAllFranchiseUseCase getAllFranchiseUseCase;
    
    @Mock
    private GetByIdFranchiseUseCase getByIdFranchiseUseCase;
    
    @Mock
    private DeleteByIdFranchiseUseCase deleteByIdFranchiseUseCase;
    
    @Mock
    private UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    @InjectMocks
    private FranchiseController franchiseController;

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
        CreateFranchiseRequest request = new CreateFranchiseRequest();
        request.setName("Test Franchise");
        
        when(createFranchiseUseCase.execute("Test Franchise")).thenReturn(Mono.just(testFranchise));

        StepVerifier.create(franchiseController.createFranchise(request))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.CREATED &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar error al crear franquicia")
    void shouldHandleErrorWhenCreatingFranchise() {
        CreateFranchiseRequest request = new CreateFranchiseRequest();
        request.setName("Test Franchise");
        
        when(createFranchiseUseCase.execute("Test Franchise"))
                .thenReturn(Mono.error(new IllegalArgumentException("Error de prueba")));

        StepVerifier.create(franchiseController.createFranchise(request))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.BAD_REQUEST &&
                    response.getBody() != null &&
                    !response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener franquicia por ID")
    void shouldGetFranchiseById() {
        when(getByIdFranchiseUseCase.execute(1L)).thenReturn(Mono.just(testFranchise));

        StepVerifier.create(franchiseController.getFranchiseById(1L))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar 404 cuando la franquicia no existe")
    void shouldReturn404WhenFranchiseNotFound() {
        when(getByIdFranchiseUseCase.execute(1L)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseController.getFranchiseById(1L))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.NOT_FOUND &&
                    response.getBody() != null &&
                    !response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe eliminar franquicia exitosamente")
    void shouldDeleteFranchiseSuccessfully() {
        when(deleteByIdFranchiseUseCase.execute(1L)).thenReturn(Mono.just(true));

        StepVerifier.create(franchiseController.deleteFranchise(1L))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar 404 al eliminar franquicia inexistente")
    void shouldReturn404WhenDeletingNonExistentFranchise() {
        when(deleteByIdFranchiseUseCase.execute(1L)).thenReturn(Mono.just(false));

        StepVerifier.create(franchiseController.deleteFranchise(1L))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.NOT_FOUND &&
                    response.getBody() != null &&
                    !response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe actualizar nombre de franquicia")
    void shouldUpdateFranchiseName() {
        UpdateNameRequest request = new UpdateNameRequest();
        request.setName("Updated Name");
        
        Franchise updatedFranchise = Franchise.builder()
                .id(1L)
                .name("Updated Name")
                .build();
        
        when(updateFranchiseNameUseCase.execute(1L, "Updated Name"))
                .thenReturn(Mono.just(updatedFranchise));

        StepVerifier.create(franchiseController.updateFranchiseName(1L, request))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }
}
