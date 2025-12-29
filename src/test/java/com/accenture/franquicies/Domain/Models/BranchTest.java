package com.accenture.franquicies.Domain.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Branch Domain Model Tests")
class BranchTest {

    private Branch branch;

    @BeforeEach
    void setUp() {
        branch = Branch.builder()
                .id(1L)
                .name("Test Branch")
                .franchiseId(1L)
                .build();
    }

    @Test
    @DisplayName("Debe crear sucursal con builder")
    void shouldCreateBranchWithBuilder() {
        // Then
        assertNotNull(branch);
        assertEquals(1L, branch.getId());
        assertEquals("Test Branch", branch.getName());
        assertEquals(1L, branch.getFranchiseId());
        assertNotNull(branch.getProducts());
        assertTrue(branch.getProducts().isEmpty());
    }

    @Test
    @DisplayName("Debe actualizar nombre correctamente")
    void shouldUpdateNameSuccessfully() {
        branch.updateName("Updated Branch");

        assertEquals("Updated Branch", branch.getName());
    }

    @Test
    @DisplayName("Debe agregar producto correctamente")
    void shouldAddProductSuccessfully() {
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .stock(100)
                .branchId(1L)
                .build();

        branch.addProduct(product);

        assertEquals(1, branch.getProducts().size());
        assertEquals("Test Product", branch.getProducts().get(0).getName());
    }

    @Test
    @DisplayName("Debe agregar múltiples productos")
    void shouldAddMultipleProducts() {
        Product product1 = Product.builder().id(1L).name("Product 1").stock(100).branchId(1L).build();
        Product product2 = Product.builder().id(2L).name("Product 2").stock(200).branchId(1L).build();

        branch.addProduct(product1);
        branch.addProduct(product2);

        assertEquals(2, branch.getProducts().size());
    }
}
