package com.sapo.mockprojectpossystem.dto.request;

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
    private String name = "Title";

    private Integer position = 1;

    @NotNull(message = "Option values are required.")
    private List<ProductOptionValueRequest> values;
}