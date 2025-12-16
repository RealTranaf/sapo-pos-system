package com.sapo.mockprojectpossystem.repository;

import com.sapo.mockprojectpossystem.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    Optional<ProductImage> findByIdAndProduct_Id(Long id, Long productId);

    List<ProductImage> findAllByProductId(Long productId);

    void deleteProductImageByIdAndProduct_id(Long id, Long productId);
}