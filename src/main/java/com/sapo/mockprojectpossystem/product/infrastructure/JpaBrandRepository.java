package com.sapo.mockprojectpossystem.product.infrastructure;

import com.sapo.mockprojectpossystem.product.domain.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaBrandRepository extends JpaRepository<Brand, Integer> {
    Optional<Brand> findByName(String name);
}
