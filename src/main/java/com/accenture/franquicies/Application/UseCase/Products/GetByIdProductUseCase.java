package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetByIdProductUseCase {

    private final ProductRepository productRepository;

    public GetByIdProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Product> execute(Long id) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("El ID del producto no puede ser nulo"));
        }

        return productRepository.findById(id);
    }
}
