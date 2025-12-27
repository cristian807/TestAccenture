package com.accenture.franquicies.Domain.Repository;

import com.accenture.franquicies.Domain.Models.Branch;

import java.util.List;
import java.util.Optional;

public interface BranchRepository {
    Branch save(Branch branch);
    List<Branch> findAll();
    List<Branch> findByFranchiseId(Long franchiseId);
    Optional<Branch> findById(Long id);
    boolean deleteById(Long id);
    Optional<Branch> updateName(Long id, String name);
}
