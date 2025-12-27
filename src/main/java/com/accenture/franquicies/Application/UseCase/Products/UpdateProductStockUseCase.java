package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateProductStockUseCase {
    private final ProductRepository productRepository;

    public UpdateProductStockUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> execute(Long id, Integer stock) {
        return productRepository.updateStock(id, stock);
    }
}
