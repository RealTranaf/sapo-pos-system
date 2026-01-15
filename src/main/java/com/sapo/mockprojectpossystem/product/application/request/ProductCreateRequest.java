package com.sapo.mockprojectpossystem.product.application.request;

import com.sapo.mockprojectpossystem.product.domain.model.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProductCreateRequest {
    @NotBlank(message = "Name is required.")
    private String name;

    private String summary;

    private String content;

    private ProductStatus status = ProductStatus.ACTIVE;

    @NotNull(message = "User id is required.")
    private Integer userId;

    private Integer brandId;

    private Set<Integer> typeIds;

    private List<ProductVariantRequest> variants;

    private List<ProductOptionRequest> options;
}