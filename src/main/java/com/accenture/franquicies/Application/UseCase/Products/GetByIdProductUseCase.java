package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetByIdProductUseCase {
    private final ProductRepository productRepository;

    public GetByIdProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> execute(Long id) {
        return productRepository.findById(id);
    }
}
