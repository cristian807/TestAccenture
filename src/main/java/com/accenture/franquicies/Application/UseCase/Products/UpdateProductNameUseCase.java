package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateProductNameUseCase {

    private final ProductRepository productRepository;

    public UpdateProductNameUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Product> execute(Long id, String newName) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("El ID del producto no puede ser nulo"));
        }

        if (newName == null || newName.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre del producto no puede estar vacío"));
        }

        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    Product updatedProduct = Product.builder()
                            .id(existingProduct.getId())
                            .name(newName.trim())
                            .stock(existingProduct.getStock())
                            .branchId(existingProduct.getBranchId())
                            .build();
                    return productRepository.save(updatedProduct);
                });
    }
}
