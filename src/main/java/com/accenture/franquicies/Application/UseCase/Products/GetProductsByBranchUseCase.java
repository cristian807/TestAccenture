package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GetProductsByBranchUseCase {

    private final ProductRepository productRepository;

    public GetProductsByBranchUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<Product> execute(Long branchId) {
        if (branchId == null) {
            return Flux.error(new IllegalArgumentException("El ID de la sucursal no puede ser nulo"));
        }

        return productRepository.findByBranchId(branchId);
    }
}
