package com.sapo.mockprojectpossystem.repository;

import com.sapo.mockprojectpossystem.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    @Query("SELECT SUM(p.amount) FROM Purchase p WHERE p.customer.id = :customerId")
    float getTotalPurchaseByCustomerId(@Param("customerId") Integer customerId);
}
