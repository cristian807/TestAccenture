package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllProductsUseCase {
    private final ProductRepository productRepository;

    public GetAllProductsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> execute() {
        return productRepository.findAll();
    }
}
