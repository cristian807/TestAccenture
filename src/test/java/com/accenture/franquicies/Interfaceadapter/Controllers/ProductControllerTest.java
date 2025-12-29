package com.accenture.franquicies.Interfaceadapter.Controllers;

import com.accenture.franquicies.Application.UseCase.Products.*;
import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.CreateProductRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.UpdateNameRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Request.UpdateStockRequest;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.ProductWithBranchResponse;
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
@DisplayName("ProductController Tests")
class ProductControllerTest {

    @Mock
    private CreateProductUseCase createProductUseCase;
    
    @Mock
    private GetAllProductsUseCase getAllProductsUseCase;
    
    @Mock
    private GetByIdProductUseCase getByIdProductUseCase;
    
    @Mock
    private GetProductsByBranchUseCase getProductsByBranchUseCase;
    
    @Mock
    private DeleteProductUseCase deleteProductUseCase;
    
    @Mock
    private UpdateProductStockUseCase updateProductStockUseCase;
    
    @Mock
    private UpdateProductNameUseCase updateProductNameUseCase;
    
    @Mock
    private GetTopStockProductsByFranchiseUseCase getTopStockProductsByFranchiseUseCase;

    @InjectMocks
    private ProductController productController;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .stock(100)
                .branchId(1L)
                .build();
    }

    @Test
    @DisplayName("Debe crear un producto exitosamente")
    void shouldCreateProductSuccessfully() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Test Product");
        request.setStock(100);
        request.setBranchId(1L);
        
        when(createProductUseCase.execute("Test Product", 100, 1L))
                .thenReturn(Mono.just(testProduct));

        StepVerifier.create(productController.createProduct(request))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.CREATED &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe manejar error al crear producto")
    void shouldHandleErrorWhenCreatingProduct() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Test Product");
        request.setStock(100);
        request.setBranchId(1L);
        
        when(createProductUseCase.execute("Test Product", 100, 1L))
                .thenReturn(Mono.error(new IllegalArgumentException("Error de prueba")));

        StepVerifier.create(productController.createProduct(request))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.BAD_REQUEST &&
                    response.getBody() != null &&
                    !response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener producto por ID")
    void shouldGetProductById() {
        when(getByIdProductUseCase.execute(1L)).thenReturn(Mono.just(testProduct));

        StepVerifier.create(productController.getProductById(1L))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe retornar 404 cuando el producto no existe")
    void shouldReturn404WhenProductNotFound() {
        when(getByIdProductUseCase.execute(1L)).thenReturn(Mono.empty());

        StepVerifier.create(productController.getProductById(1L))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.NOT_FOUND &&
                    response.getBody() != null &&
                    !response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe eliminar producto exitosamente")
    void shouldDeleteProductSuccessfully() {
        when(deleteProductUseCase.execute(1L)).thenReturn(Mono.just(true));

        StepVerifier.create(productController.deleteProduct(1L))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe actualizar stock de producto")
    void shouldUpdateProductStock() {
        UpdateStockRequest request = new UpdateStockRequest();
        request.setStock(200);
        
        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .stock(200)
                .branchId(1L)
                .build();
        
        when(updateProductStockUseCase.execute(1L, 200))
                .thenReturn(Mono.just(updatedProduct));

        StepVerifier.create(productController.updateProductStock(1L, request))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe actualizar nombre de producto")
    void shouldUpdateProductName() {
        UpdateNameRequest request = new UpdateNameRequest();
        request.setName("Updated Name");
        
        Product updatedProduct = Product.builder()
                .id(1L)
                .name("Updated Name")
                .stock(100)
                .branchId(1L)
                .build();
        
        when(updateProductNameUseCase.execute(1L, "Updated Name"))
                .thenReturn(Mono.just(updatedProduct));

        StepVerifier.create(productController.updateProductName(1L, request))
                .expectNextMatches(response -> 
                    response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    response.getBody().isSuccess())
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe obtener productos con mayor stock por franquicia")
    void shouldGetTopStockProductsByFranchise() {
        ProductWithBranchResponse response1 = ProductWithBranchResponse.builder()
                .productId(1L)
                .productName("Product 1")
                .stock(200)
                .branchId(1L)
                .branchName("Branch 1")
                .build();
        
        when(getTopStockProductsByFranchiseUseCase.execute(1L))
                .thenReturn(Flux.just(response1));

        StepVerifier.create(productController.getTopStockProductsByFranchise(1L))
                .expectNextMatches(resp -> 
                    resp.getStatusCode() == HttpStatus.OK &&
                    resp.getBody() != null &&
                    resp.getBody().isSuccess())
                .verifyComplete();
    }
}
