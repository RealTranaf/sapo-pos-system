package com.sapo.mockprojectpossystem.product.domain.repository;

import com.sapo.mockprojectpossystem.product.domain.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findBySku(String sku);

    List<ProductVariant> findByProductId(Integer product_id);

    List<ProductVariant> findAllByProduct_Id(Integer product_id);

    Optional<ProductVariant> findByIdAndProduct_id(Integer id, Integer product_id);

    void deleteByIdAndProduct_Id(Integer id, Integer product_id);
}
