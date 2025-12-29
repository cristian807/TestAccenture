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
@DisplayName("UpdateProductStockUseCase Tests")
class UpdateProductStockUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private UpdateProductStockUseCase updateProductStockUseCase;

    private Product existingProduct;
    private Product updatedProduct;

    @BeforeEach
    void setUp() {
        existingProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .stock(100)
                .branchId(1L)
                .build();
        
        updatedProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .stock(200)
                .branchId(1L)
                .build();
    }

    @Test
    @DisplayName("Debe actualizar el stock del producto exitosamente")
    void shouldUpdateProductStockSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Mono.just(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(updatedProduct));

        StepVerifier.create(updateProductStockUseCase.execute(1L, 200))
                .expectNextMatches(product -> product.getStock().equals(200))
                .verifyComplete();

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe retornar vacío cuando el producto no existe")
    void shouldReturnEmptyWhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(updateProductStockUseCase.execute(1L, 200))
                .verifyComplete();

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el ID es nulo")
    void shouldFailWhenIdIsNull() {
        StepVerifier.create(updateProductStockUseCase.execute(null, 200))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("ID del producto no puede ser nulo"))
                .verify();

        verify(productRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Debe fallar cuando el stock es negativo")
    void shouldFailWhenStockIsNegative() {
        StepVerifier.create(updateProductStockUseCase.execute(1L, -1))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("stock debe ser un número positivo"))
                .verify();

        verify(productRepository, never()).findById(anyLong());
    }
}
