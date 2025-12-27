package com.accenture.franquicies.Application.UseCase.Franchises;

import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllFranchise {
    private final FranchiseRepository franchiseRepository;

    public GetAllFranchise(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public List<Franchise> execute() {
        return franchiseRepository.findAll();
    }
}
