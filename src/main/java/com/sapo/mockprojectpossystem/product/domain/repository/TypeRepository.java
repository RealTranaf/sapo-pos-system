package com.sapo.mockprojectpossystem.product.domain.repository;

import com.sapo.mockprojectpossystem.product.domain.model.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TypeRepository {
    Optional<Type> findById(Integer id);
    Optional<Type> findByName(String name);
    List<Type> findAll();
    Page<Type> findAll(Pageable pageable);
    Type save(Type type);
    List<Type> saveAll(List<Type> type);
    long count();
}
