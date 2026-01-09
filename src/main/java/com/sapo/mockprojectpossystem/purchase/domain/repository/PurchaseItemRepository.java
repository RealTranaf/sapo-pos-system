package com.sapo.mockprojectpossystem.purchase.domain.repository;

import com.sapo.mockprojectpossystem.purchase.domain.model.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Integer> {
}
