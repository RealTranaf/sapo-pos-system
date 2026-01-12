package com.sapo.mockprojectpossystem.product.infrastructure;

import com.sapo.mockprojectpossystem.product.domain.model.Brand;
import com.sapo.mockprojectpossystem.product.domain.repository.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaBrandRepositoryAdapter implements BrandRepository {

    private final JpaBrandRepository jpaRepo;

    @Override
    public Optional<Brand> findById(Integer id) {
        return jpaRepo.findById(id);
    }

    @Override
    public Optional<Brand> findByName(String name) {
        return jpaRepo.findByName(name);
    }

    @Override
    public List<Brand> findAll() {
        return jpaRepo.findAll();
    }

    @Override
    public Page<Brand> findAll(Pageable pageable) {
        return jpaRepo.findAll(pageable);
    }

    @Override
    public Brand save(Brand brand) {
        return jpaRepo.save(brand);
    }

    @Override
    public List<Brand> saveAll(List<Brand> brands) {
        return jpaRepo.saveAll(brands);
    }

    @Override
    public long count() {
        return jpaRepo.count();
    }
}
