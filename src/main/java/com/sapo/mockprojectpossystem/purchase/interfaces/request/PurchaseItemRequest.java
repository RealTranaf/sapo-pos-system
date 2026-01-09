package com.sapo.mockprojectpossystem.purchase.interfaces.request;

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
