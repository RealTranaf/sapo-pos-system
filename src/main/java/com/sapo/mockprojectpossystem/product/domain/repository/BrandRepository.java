package com.sapo.mockprojectpossystem.product.domain.repository;

import com.sapo.mockprojectpossystem.product.domain.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Optional<Brand> findById(Integer id);
    Optional<Brand> findByName(String name);
    List<Brand> findAll();
    Page<Brand> findAll(Pageable pageable);
    Brand save(Brand brand);
    List<Brand> saveAll(List<Brand> brands);
    long count();
}
