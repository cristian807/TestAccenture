package com.accenture.franquicies.Domain.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Product {
    private final Long id;
    private String name;
    private Integer stock;
    private final Long branchId;

    public void updateStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser nulo ni negativo");
        }
        this.stock = stock;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
