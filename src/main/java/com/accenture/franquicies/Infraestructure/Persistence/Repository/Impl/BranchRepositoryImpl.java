package com.accenture.franquicies.Infraestructure.Persistence.Repository.Impl;

import com.accenture.franquicies.Domain.Models.Branch;
import com.accenture.franquicies.Domain.Repository.BranchRepository;
import com.accenture.franquicies.Infraestructure.Persistence.Entity.BranchEntity;
import com.accenture.franquicies.Infraestructure.Persistence.Mappers.BranchMapper;
import com.accenture.franquicies.Infraestructure.Persistence.Repository.JpaBranchRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class BranchRepositoryImpl implements BranchRepository {

    private final JpaBranchRepository jpaBranchRepository;
    private final BranchMapper branchMapper;

    public BranchRepositoryImpl(JpaBranchRepository jpaBranchRepository,
                                BranchMapper branchMapper) {
        this.jpaBranchRepository = jpaBranchRepository;
        this.branchMapper = branchMapper;
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        BranchEntity entity = branchMapper.toEntity(branch);
        return jpaBranchRepository.save(entity)
                .map(branchMapper::toDomain);
    }

    @Override
    public Mono<Branch> findById(Long id) {
        return jpaBranchRepository.findById(id)
                .map(branchMapper::toDomain);
    }

    @Override
    public Flux<Branch> findAll() {
        return jpaBranchRepository.findAll()
                .map(branchMapper::toDomain);
    }

    @Override
    public Flux<Branch> findByFranchiseId(Long franchiseId) {
        return jpaBranchRepository.findByFranchiseId(franchiseId)
                .map(branchMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return jpaBranchRepository.deleteById(id);
    }
}
