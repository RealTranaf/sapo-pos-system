package com.sapo.mockprojectpossystem.product.interfaces.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionRequest {
    private String name;

    private Integer position;

    @NotNull(message = "Option values are required.")
    private List<ProductOptionValueRequest> values;
}