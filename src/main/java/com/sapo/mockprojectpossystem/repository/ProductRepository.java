package com.sapo.mockprojectpossystem.repository;

import com.sapo.mockprojectpossystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByBarcode(String barcode);
    Optional<Product> findBySku(String sku);
}
