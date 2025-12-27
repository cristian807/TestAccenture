package com.accenture.franquicies.Application.UseCase.Products;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Models.Product;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import com.accenture.franquicies.Domain.Repository.ProductRepository;
import com.accenture.franquicies.Interfaceadapter.Controllers.DTO.Response.ProductWithBranchResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetTopStockProductsByFranchiseUseCase {
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public GetTopStockProductsByFranchiseUseCase(ProductRepository productRepository, 
                                                  BranchRepository branchRepository) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
    }

    public List<ProductWithBranchResponse> execute(Long franchiseId) {
        List<Branch> branches = branchRepository.findByFranchiseId(franchiseId);
        
        return branches.stream()
                .map(branch -> {
                    Optional<Product> topProduct = productRepository.findTopStockByBranchId(branch.getId());
                    return topProduct.map(product -> new ProductWithBranchResponse(
                            product.getId(),
                            product.getName(),
                            product.getStock(),
                            branch.getId(),
                            branch.getName()
                    ));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
