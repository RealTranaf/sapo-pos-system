package com.sapo.mockprojectpossystem.product.interfaces.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantRequest {
    private String sku;

    private String barcode;

    private BigDecimal price;

    private BigDecimal compareAtPrice;

    private String title;

    private String option1;

    private String option2;

    private String option3;

    private Boolean taxable = false;

    private Integer inventoryQuantity = 0;

    private String unit;

    private Integer imageId;

    private Integer position = 1;
}