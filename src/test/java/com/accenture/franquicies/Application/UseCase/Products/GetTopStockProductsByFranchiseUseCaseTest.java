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
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetTopStockProductsByFranchiseUseCase Tests")
class GetTopStockProductsByFranchiseUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetTopStockProductsByFranchiseUseCase getTopStockProductsByFranchiseUseCase;

    private Branch testBranch1;
    private Branch testBranch2;
    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        testBranch1 = Branch.builder().id(1L).name("Branch 1").franchiseId(1L).build();
        testBranch2 = Branch.builder().id(2L).name("Branch 2").franchiseId(1L).build();
        
        product1 = Product.builder().id(1L).name("Product 1").stock(100).branchId(1L).build();
        product2 = Product.builder().id(2L).name("Product 2").stock(200).branchId(1L).build();
        product3 = Product.builder().id(3L).name("Product 3").stock(150).branchId(2L).build();
    }

    @Test
    @DisplayName("Debe retornar el producto con mayor stock de cada sucursal")
    void shouldReturnTopStockProductsByBranch() {
        when(branchRepository.findByFranchiseId(1L)).thenReturn(Flux.just(testBranch1, testBranch2));
        when(productRepository.findByBranchId(1L)).thenReturn(Flux.just(product1, product2));
        when(productRepository.findByBranchId(2L)).thenReturn(Flux.just(product3));

        StepVerifier.create(getTopStockProductsByFranchiseUseCase.execute(1L))
                .expectNextMatches(response -> 
                    response.getProductName().equals("Product 2") && 
                    response.getStock().equals(200) &&
                    response.getBranchName().equals("Branch 1"))
                .expectNextMatches(response -> 
                    response.getProductName().equals("Product 3") && 
                    response.getStock().equals(150) &&
                    response.getBranchName().equals("Branch 2"))
                .verifyComplete();

        verify(branchRepository, times(1)).findByFranchiseId(1L);
    }

    @Test
    @DisplayName("Debe fallar cuando el franchiseId es nulo")
    void shouldFailWhenFranchiseIdIsNull() {
        StepVerifier.create(getTopStockProductsByFranchiseUseCase.execute(null))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException &&
                    throwable.getMessage().contains("ID de la franquicia no puede ser nulo"))
                .verify();

        verify(branchRepository, never()).findByFranchiseId(anyLong());
    }

    @Test
    @DisplayName("Debe retornar vacío cuando la franquicia no tiene sucursales")
    void shouldReturnEmptyWhenNoMatchingBranches() {
        when(branchRepository.findByFranchiseId(1L)).thenReturn(Flux.empty());

        StepVerifier.create(getTopStockProductsByFranchiseUseCase.execute(1L))
                .verifyComplete();

        verify(branchRepository, times(1)).findByFranchiseId(1L);
    }
}
