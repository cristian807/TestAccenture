package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateProductNameUseCase {
    private final ProductRepository productRepository;

    public UpdateProductNameUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> execute(Long id, String name) {
        return productRepository.updateName(id, name);
    }
}
