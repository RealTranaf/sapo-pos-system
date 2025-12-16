package com.sapo.mockprojectpossystem.repository;

import com.sapo.mockprojectpossystem.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p LEFT JOIN p.variants v " +
        "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(v.sku) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "ORDER BY p.modifiedOn DESC")
    Page<Product> searchByNameOrSku(@Param("search") String search, Pageable pageable);
}
