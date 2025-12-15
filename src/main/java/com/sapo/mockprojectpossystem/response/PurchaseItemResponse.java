package com.sapo.mockprojectpossystem.response;

import com.sapo.mockprojectpossystem.model.PurchaseItem;
import lombok.Data;

@Data
public class PurchaseItemResponse {
    private Integer id;
    private Integer productId;
    private Integer purchaseId;
    private int quantity;
    private double totalPrice;

    public PurchaseItemResponse(PurchaseItem purchaseItem) {
        this.id = purchaseItem.getId();
        this.productId = purchaseItem.getProduct().getId();
        this.purchaseId = purchaseItem.getPurchase().getId();
        this.quantity = purchaseItem.getQuantity();
        this.totalPrice = purchaseItem.getTotalPrice();
    }
}
