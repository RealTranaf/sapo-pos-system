package com.sapo.mockprojectpossystem.purchase.interfaces.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequest {
    @NotBlank
    private Integer customerId;
    @NotBlank
    private Integer userId;
    private double totalAmount;
    private double discountAmount;
    private String note;
    private List<PurchaseItemRequest> purchaseItems;
}
