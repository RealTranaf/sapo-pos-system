package com.sapo.mockprojectpossystem.repository;

import com.sapo.mockprojectpossystem.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findBySku(String sku);

    List<ProductVariant> findByProductId(Long productId);

    List<ProductVariant> findAllByProduct_Id(Long productId);

    Optional<ProductVariant> findByIdAndProduct_id(Long id, Long productId);

    void deleteByIdAndProduct_Id(Long id, Long productId);
}
