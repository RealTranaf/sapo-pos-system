package com.sapo.mockprojectpossystem.purchase.application.request;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItemRequest {
    private Integer productVariantId;
    private int quantity;
}
