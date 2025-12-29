package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public CreateProductUseCase(ProductRepository productRepository, BranchRepository branchRepository) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
    }

    public Mono<Product> execute(String name, Integer stock, Long branchId) {
        if (name == null || name.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre del producto es requerido"));
        }

        if (stock == null || stock < 0) {
            return Mono.error(new IllegalArgumentException("El stock debe ser un número positivo"));
        }

        if (branchId == null) {
            return Mono.error(new IllegalArgumentException("El ID de la sucursal es requerido"));
        }

        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La sucursal con ID " + branchId + " no existe")))
                .flatMap(branch -> {
                    Product product = Product.builder()
                            .name(name.trim())
                            .stock(stock)
                            .branchId(branchId)
                            .build();
                    return productRepository.save(product);
                });
    }
}
