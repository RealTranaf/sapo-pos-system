package com.sapo.mockprojectpossystem.product.interfaces.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("barcode")
    private String barcode;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("compare_at_price")
    private BigDecimal compareAtPrice;

    @JsonProperty("base_price")
    private BigDecimal basePrice;

    @JsonProperty("title")
    private String title;

    @JsonProperty("option1")
    private String option1;

    @JsonProperty("option2")
    private String option2;

    @JsonProperty("option3")
    private String option3;

    @JsonProperty("taxable")
    private Boolean taxable;

    @JsonProperty("inventory_quantity")
    private Integer inventoryQuantity;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("image")
    private ProductImageResponse image;

    @JsonProperty("position")
    private Integer position;
}
