package com.accenture.franquicies.Domain.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Branch {
    private final Long id;
    private String name;
    private final Long franchiseId;
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    public void updateName(String name) {
        this.name = name;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }
}
