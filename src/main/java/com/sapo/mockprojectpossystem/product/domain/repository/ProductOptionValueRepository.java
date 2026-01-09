package com.sapo.mockprojectpossystem.product.domain.repository;

import com.sapo.mockprojectpossystem.product.domain.model.ProductOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionValueRepository extends JpaRepository<ProductOptionValue, Long> {
}
