package com.sapo.mockprojectpossystem.product.domain.repository;

import com.sapo.mockprojectpossystem.product.domain.model.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    List<ProductOption> findByProductId(Long productId);
}