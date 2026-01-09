package com.sapo.mockprojectpossystem.purchase.interfaces.response;

import com.sapo.mockprojectpossystem.purchase.domain.model.PurchaseItem;
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
        this.productId = purchaseItem.getProductVariant().getId();
        this.purchaseId = purchaseItem.getPurchase().getId();
        this.quantity = purchaseItem.getQuantity();
        this.totalPrice = purchaseItem.getTotalPrice();
    }
}
