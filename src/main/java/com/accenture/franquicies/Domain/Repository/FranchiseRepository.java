package com.accenture.franquicies.Domain.Repository;

import com.accenture.franquicies.Domain.Models.Franchise;

import java.util.List;
import java.util.Optional;

public interface FranchiseRepository {
    Franchise save(Franchise franchise);
    List<Franchise> findAll();
    Optional<Franchise> findById(Long id);
    boolean deleteById(Long id);
    Optional<Franchise> updateName(Long id, String name);
}
