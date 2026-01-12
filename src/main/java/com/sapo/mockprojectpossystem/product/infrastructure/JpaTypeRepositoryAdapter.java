package com.sapo.mockprojectpossystem.product.infrastructure;

import com.sapo.mockprojectpossystem.product.domain.model.Type;
import com.sapo.mockprojectpossystem.product.domain.repository.TypeRepository;
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
public class JpaTypeRepositoryAdapter implements TypeRepository {

    private final JpaTypeRepository jpaRepo;

    @Override
    public Optional<Type> findById(Integer id) {
        return jpaRepo.findById(id);
    }

    @Override
    public Optional<Type> findByName(String name) {
        return jpaRepo.findByName(name);
    }

    @Override
    public List<Type> findAll() {
        return jpaRepo.findAll();
    }

    @Override
    public Page<Type> findAll(Pageable pageable) {
        return jpaRepo.findAll(pageable);
    }

    @Override
    public Type save(Type type) {
        return jpaRepo.save(type);
    }

    @Override
    public List<Type> saveAll(List<Type> type) {
        return jpaRepo.saveAll(type);
    }

    @Override
    public long count() {
        return jpaRepo.count();
    }
}
