package com.accenture.franquicies.Interfaceadapter.Controllers;

import com.accenture.franquicies.Application.UseCase.Branches.*;
import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.CreateBranchRequest;
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
@DisplayName("BranchController Tests")
class BranchControllerTest {

    @Mock
    private CreateBranchUseCase createBranchUseCase;
    
    @Mock
    private GetAllBranchesUseCase getAllBranchesUseCase;
    
    @Mock
    private GetByIdBranchUseCase getByIdBranchUseCase;
    
    @Mock
    private GetBranchesByFranchiseUseCase getBranchesByFranchiseUseCase;
    
    @Mock
    private DeleteBranchUseCase deleteBranchUseCase;
    
    @Mock
    private UpdateBranchNameUseCase updateBranchNameUseCase;

    @InjectMocks
    private BranchController branchController;

    private Branch testBranch;

    @BeforeEach
    void setUp() {
        testBranch = Branch.builder()
                .id(1L)
                .name("Test Branch")
                .franchiseId(1L)
                .build();
    }

    @Test
    @DisplayName("Debe crear una sucursal exitosamente")
    void shouldCreateBranchSuccessfully() {
        CreateBranchRequest request = new CreateBranchRequest();
        request.setName("Test Branch");
        request.setFranchiseId(1L);
        
        when(createBranchUseCase.execute("Test Branch", 1L)).thenReturn(Mono.just(testBranch));

        StepVerifier.create(branchController.createBranch(request))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.CREATED &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar error al crear sucursal")
    void shouldHandleErrorWhenCreatingBranch() {
        CreateBranchRequest request = new CreateBranchRequest();
        request.setName("Test Branch");
        request.setFranchiseId(1L);
        
        when(createBranchUseCase.execute("Test Branch", 1L))
                .thenReturn(Mono.error(new IllegalArgumentException("Error de prueba")));

        StepVerifier.create(branchController.createBranch(request))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.BAD_REQUEST &&
                    response.getBody() != null &&
                    !response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener sucursal por ID")
    void shouldGetBranchById() {
        when(getByIdBranchUseCase.execute(1L)).thenReturn(Mono.just(testBranch));

        StepVerifier.create(branchController.getBranchById(1L))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar 404 cuando la sucursal no existe")
    void shouldReturn404WhenBranchNotFound() {
        when(getByIdBranchUseCase.execute(1L)).thenReturn(Mono.empty());

        StepVerifier.create(branchController.getBranchById(1L))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.NOT_FOUND &&
                    response.getBody() != null &&
                    !response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe eliminar sucursal exitosamente")
    void shouldDeleteBranchSuccessfully() {
        when(deleteBranchUseCase.execute(1L)).thenReturn(Mono.just(true));

        StepVerifier.create(branchController.deleteBranch(1L))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe actualizar nombre de sucursal")
    void shouldUpdateBranchName() {
        UpdateNameRequest request = new UpdateNameRequest();
        request.setName("Updated Name");
        
        Branch updatedBranch = Branch.builder()
                .id(1L)
                .name("Updated Name")
                .franchiseId(1L)
                .build();
        
        when(updateBranchNameUseCase.execute(1L, "Updated Name"))
                .thenReturn(Mono.just(updatedBranch));

        StepVerifier.create(branchController.updateBranchName(1L, request))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }
}
