package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.ProductWithBranchResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GetTopStockProductsByFranchiseUseCase {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public GetTopStockProductsByFranchiseUseCase(BranchRepository branchRepository,
                                                 ProductRepository productRepository) {
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
    }

    public Flux<ProductWithBranchResponse> execute(Long franchiseId) {
        if (franchiseId == null) {
            return Flux.error(new IllegalArgumentException("El ID de la franquicia no puede ser nulo"));
        }

        return branchRepository.findByFranchiseId(franchiseId)
                .flatMap(branch -> productRepository.findByBranchId(branch.getId())
                        .sort((p1, p2) -> Integer.compare(p2.getStock(), p1.getStock()))
                        .take(1)
                        .map(product -> ProductWithBranchResponse.builder()
                                .productId(product.getId())
                                .productName(product.getName())
                                .stock(product.getStock())
                                .branchId(branch.getId())
                                .branchName(branch.getName())
                                .build())
                );
    }
}
