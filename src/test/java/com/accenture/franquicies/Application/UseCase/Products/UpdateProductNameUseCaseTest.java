package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
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
@DisplayName("UpdateProductNameUseCase Tests")
class UpdateProductNameUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private UpdateProductNameUseCase updateProductNameUseCase;

    private Product existingProduct;
    private Product updatedProduct;

    @BeforeEach
    void setUp() {
        existingProduct = Product.builder()
                .id(1L)
                .name("Old Name")
                .stock(100)
                .branchId(1L)
                .build();
        
        updatedProduct = Product.builder()
                .id(1L)
                .name("New Name")
                .stock(100)
                .branchId(1L)
                .build();
    }

    @Test
    @DisplayName("Debe actualizar el nombre del producto exitosamente")
    void shouldUpdateProductNameSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Mono.just(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(updatedProduct));

        StepVerifier.create(updateProductNameUseCase.execute(1L, "New Name"))
                .expectNextMatches(product -> product.getName().equals("New Name"))
                .verifyComplete();

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe retornar vacío cuando el producto no existe")
    void shouldReturnEmptyWhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(updateProductNameUseCase.execute(1L, "New Name"))
                .verifyComplete();

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el ID es nulo")
    void shouldFailWhenIdIsNull() {
        StepVerifier.create(updateProductNameUseCase.execute(null, "New Name"))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("ID del producto no puede ser nulo"))
                .verify();

        verify(productRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Debe fallar cuando el nombre es vacío")
    void shouldFailWhenNameIsEmpty() {
        StepVerifier.create(updateProductNameUseCase.execute(1L, ""))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("nombre del producto no puede estar vacío"))
                .verify();

        verify(productRepository, never()).findById(anyLong());
    }
}
