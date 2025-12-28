package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateProductUseCase {
    private final ProductRepository productRepository;

    public CreateProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(String name, Integer stock, Long branchId) {
        if (stock == null) {
            throw new IllegalArgumentException("El stock no puede ser nulo");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        Product product = Product.builder()
                .id(null)
                .name(name)
                .stock(stock)
                .branchId(branchId)
                .build();
        return productRepository.save(product);
    }
}
