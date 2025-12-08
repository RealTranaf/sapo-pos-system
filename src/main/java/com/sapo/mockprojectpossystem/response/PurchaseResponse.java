package com.sapo.mockprojectpossystem.response;

import com.sapo.mockprojectpossystem.model.Purchase;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchaseResponse {
    private Integer id;
    private Integer customerId;
    private float amount;
    private LocalDateTime purchaseDate;
    private String note;

    public PurchaseResponse(Purchase purchase) {
        this.id = purchase.getId();
        this.customerId = purchase.getCustomer().getId();
        this.amount = purchase.getAmount();
        this.purchaseDate = purchase.getPurchaseDate();
        this.note = purchase.getNote();
    }
}
