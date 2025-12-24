package com.sapo.mockprojectpossystem.repository;

import com.sapo.mockprojectpossystem.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT DISTINCT p FROM Product p " +
        "LEFT JOIN p.variants v " +
        "LEFT JOIN p.types t " +
        "WHERE (:search IS NULL OR :search = '' " +
        "      OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "      OR LOWER(v.sku) LIKE LOWER(CONCAT('%', :search, '%'))) " +
        "AND p.status = 'ACTIVE' " +
        "AND (:brandId IS NULL OR :brandId = 0 OR p.brand.id = :brandId) " +
        "AND (:typeIds IS NULL OR t.id IN :typeIds) " +
        "ORDER BY p.updatedAt DESC")
    Page<Product> searchProducts(
        @Param("search") String search,
        @Param("brandId") Integer brandId,
        @Param("typeIds") List<Integer> typeIds,
        Pageable pageable
    );
}
