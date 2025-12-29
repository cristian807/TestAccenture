package com.accenture.franquicies.Domain.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Domain Model Tests")
class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .stock(100)
                .branchId(1L)
                .build();
    }

    @Test
    @DisplayName("Debe crear producto con builder")
    void shouldCreateProductWithBuilder() {
        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(100, product.getStock());
        assertEquals(1L, product.getBranchId());
    }

    @Test
    @DisplayName("Debe actualizar stock correctamente")
    void shouldUpdateStockSuccessfully() {
        product.updateStock(200);

        assertEquals(200, product.getStock());
    }

    @Test
    @DisplayName("Debe fallar al actualizar stock con valor nulo")
    void shouldFailUpdateStockWithNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> product.updateStock(null)
        );
        assertTrue(exception.getMessage().contains("stock no puede ser nulo"));
    }

    @Test
    @DisplayName("Debe fallar al actualizar stock con valor negativo")
    void shouldFailUpdateStockWithNegativeValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> product.updateStock(-10)
        );
        assertTrue(exception.getMessage().contains("stock no puede ser nulo ni negativo"));
    }

    @Test
    @DisplayName("Debe actualizar nombre correctamente")
    void shouldUpdateNameSuccessfully() {
        product.updateName("New Product Name");

        assertEquals("New Product Name", product.getName());
    }

    @Test
    @DisplayName("Debe permitir stock en cero")
    void shouldAllowZeroStock() {
        product.updateStock(0);
        assertEquals(0, product.getStock());
    }
}
