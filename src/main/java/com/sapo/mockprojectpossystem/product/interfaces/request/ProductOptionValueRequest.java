package com.sapo.mockprojectpossystem.product.interfaces.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionValueRequest {
    private String value;
}
