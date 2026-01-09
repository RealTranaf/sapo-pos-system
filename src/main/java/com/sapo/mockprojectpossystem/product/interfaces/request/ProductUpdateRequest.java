package com.sapo.mockprojectpossystem.product.interfaces.request;

import com.sapo.mockprojectpossystem.product.domain.enums.ProductStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {
    private String name;

    private String summary;

    private String content;

    private ProductStatus status;

    private Integer brandId;

    private Set<Integer> typeIds;

    private List<ProductVariantRequest> variants;

    @Valid
    private List<ProductOptionRequest> options;
}