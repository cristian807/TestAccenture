package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteProductUseCase {
    private final ProductRepository productRepository;

    public DeleteProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean execute(Long id) {
        return productRepository.deleteById(id);
    }
}
