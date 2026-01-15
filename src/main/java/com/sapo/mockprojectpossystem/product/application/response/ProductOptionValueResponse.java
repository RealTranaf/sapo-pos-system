package com.sapo.mockprojectpossystem.product.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionValueResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("value")
    private String value;
}