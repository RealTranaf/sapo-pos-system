package com.sapo.mockprojectpossystem.request;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequest {
    private Integer customerId;
    private Integer userId;
    private double totalAmount;
    private double discountAmount;
    private String note;
    private List<PurchaseItemRequest> purchaseItems;
}
