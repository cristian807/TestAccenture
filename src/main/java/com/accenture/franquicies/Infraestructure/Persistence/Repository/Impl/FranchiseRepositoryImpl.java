package com.accenture.franquicies.Infraestructure.Persistence.Repository.Impl;

import com.accenture.franquicies.Domain.Models.Franchise;
import com.accenture.franquicies.Domain.Repository.FranchiseRepository;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.FranchiseEntity;
import com.accenture.franquicies.Infraestructure.Persistence.Mappers.FranchiseMapper;
import com.accenture.franquicies.Infraestructure.Persistence.Repository.JpaFranchiseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class FranchiseRepositoryImpl implements FranchiseRepository {

    private final JpaFranchiseRepository jpaFranchiseRepository;
    private final FranchiseMapper franchiseMapper;

    public FranchiseRepositoryImpl(JpaFranchiseRepository jpaFranchiseRepository, FranchiseMapper franchiseMapper) {
        this.jpaFranchiseRepository = jpaFranchiseRepository;
        this.franchiseMapper = franchiseMapper;
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseEntity entity = franchiseMapper.toEntity(franchise);
        return jpaFranchiseRepository.save(entity)
                .map(franchiseMapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return jpaFranchiseRepository.findById(id)
                .map(franchiseMapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return jpaFranchiseRepository.findAll()
                .map(franchiseMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return jpaFranchiseRepository.deleteById(id);
    }
}
