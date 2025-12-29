package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
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
@DisplayName("GetAllProductsUseCase Tests")
class GetAllProductsUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetAllProductsUseCase getAllProductsUseCase;

    @Test
    @DisplayName("Debe retornar todos los productos")
    void shouldReturnAllProducts() {
        Product product1 = Product.builder().id(1L).name("Product 1").stock(100).branchId(1L).build();
        Product product2 = Product.builder().id(2L).name("Product 2").stock(200).branchId(1L).build();
        
        when(productRepository.findAll()).thenReturn(Flux.just(product1, product2));

        StepVerifier.create(getAllProductsUseCase.execute())
                .expectNext(product1)
                .expectNext(product2)
                .verifyComplete();

        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay productos")
    void shouldReturnEmptyWhenNoProducts() {
        when(productRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(getAllProductsUseCase.execute())
                .verifyComplete();

        verify(productRepository, times(1)).findAll();
    }
}
