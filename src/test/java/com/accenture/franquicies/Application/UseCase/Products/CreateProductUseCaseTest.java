package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
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
@DisplayName("CreateProductUseCase Tests")
class CreateProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    private Branch testBranch;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testBranch = Branch.builder()
                .id(1L)
                .name("Test Branch")
                .franchiseId(1L)
                .build();
        
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
        when(branchRepository.findById(1L)).thenReturn(Mono.just(testBranch));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(testProduct));

        StepVerifier.create(createProductUseCase.execute("Test Product", 100, 1L))
                .expectNextMatches(product -> 
                    product.getId().equals(1L) && 
                    product.getName().equals("Test Product") &&
                    product.getStock().equals(100))
                .verifyComplete();

        verify(branchRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe fallar cuando la sucursal no existe")
    void shouldFailWhenBranchNotFound() {
        when(branchRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(createProductUseCase.execute("Test Product", 100, 1L))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("no existe"))
                .verify();

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el nombre es nulo")
    void shouldFailWhenNameIsNull() {
        StepVerifier.create(createProductUseCase.execute(null, 100, 1L))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("nombre del producto es requerido"))
                .verify();

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe fallar cuando el stock es negativo")
    void shouldFailWhenStockIsNegative() {
        StepVerifier.create(createProductUseCase.execute("Test Product", -1, 1L))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("stock debe ser un número positivo"))
                .verify();

        verify(productRepository, never()).save(any(Product.class));
    }
}
