package com.sapo.mockprojectpossystem.product.domain.repository;

import com.sapo.mockprojectpossystem.product.domain.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    Optional<ProductImage> findByIdAndProduct_Id(Integer id, Integer productId);

    List<ProductImage> findAllByProductId(Integer productId);

    void deleteProductImageByIdAndProduct_id(Integer id, Integer productId);
}