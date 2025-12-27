package com.accenture.franquicies.Infraestructure.Persistence.Repository.Impl;

import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.FranchiseEntity;
import com.accenture.franquicies.Infraestructure.Persistence.Mappers.FranchiseMapper;
import com.accenture.franquicies.Infraestructure.Persistence.Repository.JpaFranchiseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FranchiseRepositoryImpl implements FranchiseRepository {

    private final JpaFranchiseRepository jpaFranchiseRepository;
    private final FranchiseMapper franchiseMapper;

    public FranchiseRepositoryImpl(JpaFranchiseRepository jpaFranchiseRepository, FranchiseMapper franchiseMapper) {
        this.jpaFranchiseRepository = jpaFranchiseRepository;
        this.franchiseMapper = franchiseMapper;
    }

    @Override
    public Franchise save(Franchise franchise) {
        FranchiseEntity entity = franchiseMapper.toEntity(franchise);
        FranchiseEntity savedEntity = jpaFranchiseRepository.save(entity);
        return franchiseMapper.toDomainSimple(savedEntity);
    }

    @Override
    public List<Franchise> findAll() {
        return jpaFranchiseRepository.findAll().stream()
                .map(franchiseMapper::toDomainSimple)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Franchise> findById(Long id) {
        return jpaFranchiseRepository.findById(id)
                .map(franchiseMapper::toDomainSimple);
    }

    @Override
    public boolean deleteById(Long id) {
        if (jpaFranchiseRepository.existsById(id)) {
            jpaFranchiseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Franchise> updateName(Long id, String name) {
        return jpaFranchiseRepository.findById(id)
                .map(entity -> {
                    entity.setName(name);
                    FranchiseEntity savedEntity = jpaFranchiseRepository.save(entity);
                    return franchiseMapper.toDomainSimple(savedEntity);
                });
    }
}
