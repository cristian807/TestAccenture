package com.accenture.franquicies.Infraestructure.Persistence.Repository.Impl;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.BranchEntity;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.FranchiseEntity;
import com.accenture.franquicies.Infraestructure.Persistence.Mappers.BranchMapper;
import com.accenture.franquicies.Infraestructure.Persistence.Repository.JpaBranchRepository;
import com.accenture.franquicies.Infraestructure.Persistence.Repository.JpaFranchiseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BranchRepositoryImpl implements BranchRepository {

    private final JpaBranchRepository jpaBranchRepository;
    private final JpaFranchiseRepository jpaFranchiseRepository;
    private final BranchMapper branchMapper;

    public BranchRepositoryImpl(JpaBranchRepository jpaBranchRepository,
                                JpaFranchiseRepository jpaFranchiseRepository,
                                BranchMapper branchMapper) {
        this.jpaBranchRepository = jpaBranchRepository;
        this.jpaFranchiseRepository = jpaFranchiseRepository;
        this.branchMapper = branchMapper;
    }

    @Override
    public Branch save(Branch branch) {
        FranchiseEntity franchiseEntity = jpaFranchiseRepository.findById(branch.getFranchiseId())
                .orElseThrow(() -> new RuntimeException("Franchise not found with id: " + branch.getFranchiseId()));
        
        BranchEntity entity = branchMapper.toEntity(branch, franchiseEntity);
        BranchEntity savedEntity = jpaBranchRepository.save(entity);
        return branchMapper.toDomainSimple(savedEntity);
    }

    @Override
    public List<Branch> findAll() {
        return jpaBranchRepository.findAll().stream()
                .map(branchMapper::toDomainSimple)
                .collect(Collectors.toList());
    }

    @Override
    public List<Branch> findByFranchiseId(Long franchiseId) {
        return jpaBranchRepository.findByFranchiseId(franchiseId).stream()
                .map(branchMapper::toDomainSimple)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Branch> findById(Long id) {
        return jpaBranchRepository.findById(id)
                .map(branchMapper::toDomainSimple);
    }

    @Override
    public boolean deleteById(Long id) {
        if (jpaBranchRepository.existsById(id)) {
            jpaBranchRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Branch> updateName(Long id, String name) {
        return jpaBranchRepository.findById(id)
                .map(entity -> {
                    entity.setName(name);
                    BranchEntity savedEntity = jpaBranchRepository.save(entity);
                    return branchMapper.toDomainSimple(savedEntity);
                });
    }
}
