package com.sapo.mockprojectpossystem.purchase.interfaces.response;

import com.sapo.mockprojectpossystem.purchase.domain.model.Purchase;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PurchaseResponse {
    private Integer id;
    private Integer customerId;
    private Integer userId;
    private double totalAmount;
    private double discountAmount;
    private String note;
    private LocalDateTime createdAt;
    private List<PurchaseItemResponse> purchaseItems;

    public PurchaseResponse(Purchase purchase) {
        this.id = purchase.getId();
        this.customerId = purchase.getCustomer().getId();
        this.userId = purchase.getUser().getId();
        this.totalAmount = purchase.getTotalAmount();
        this.discountAmount = purchase.getDiscountAmount();
        this.note = purchase.getNote();
        this.createdAt = purchase.getCreatedAt();
        this.purchaseItems = purchase.getPurchaseItems()
                .stream()
                .map(PurchaseItemResponse::new)
                .collect(Collectors.toList());
    }
}
