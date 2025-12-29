package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateProductStockUseCase {

    private final ProductRepository productRepository;

    public UpdateProductStockUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Product> execute(Long id, Integer newStock) {
        if (id == null) {
            return Mono.error(new IllegalArgumentException("El ID del producto no puede ser nulo"));
        }

        if (newStock == null || newStock < 0) {
            return Mono.error(new IllegalArgumentException("El stock debe ser un número positivo"));
        }

        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    Product updatedProduct = Product.builder()
                            .id(existingProduct.getId())
                            .name(existingProduct.getName())
                            .stock(newStock)
                            .branchId(existingProduct.getBranchId())
                            .build();
                    return productRepository.save(updatedProduct);
                });
    }
}
