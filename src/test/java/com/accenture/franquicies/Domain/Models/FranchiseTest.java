package com.accenture.franquicies.Domain.Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Franchise Domain Model Tests")
class FranchiseTest {

    private Franchise franchise;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder()
                .id(1L)
                .name("Test Franchise")
                .build();
    }

    @Test
    @DisplayName("Debe crear franquicia con builder")
    void shouldCreateFranchiseWithBuilder() {
        assertNotNull(franchise);
        assertEquals(1L, franchise.getId());
        assertEquals("Test Franchise", franchise.getName());
        assertNotNull(franchise.getBranches());
        assertTrue(franchise.getBranches().isEmpty());
    }

    @Test
    @DisplayName("Debe actualizar nombre correctamente")
    void shouldUpdateNameSuccessfully() {
        franchise.updateName("Updated Franchise");

        assertEquals("Updated Franchise", franchise.getName());
    }

    @Test
    @DisplayName("Debe agregar sucursal correctamente")
    void shouldAddBranchSuccessfully() {
        Branch branch = Branch.builder()
                .id(1L)
                .name("Test Branch")
                .franchiseId(1L)
                .build();

        franchise.addBranch(branch);

        assertEquals(1, franchise.getBranches().size());
        assertEquals("Test Branch", franchise.getBranches().get(0).getName());
    }

    @Test
    @DisplayName("Debe agregar múltiples sucursales")
    void shouldAddMultipleBranches() {
        Branch branch1 = Branch.builder().id(1L).name("Branch 1").franchiseId(1L).build();
        Branch branch2 = Branch.builder().id(2L).name("Branch 2").franchiseId(1L).build();

        franchise.addBranch(branch1);
        franchise.addBranch(branch2);

        assertEquals(2, franchise.getBranches().size());
    }
}
