package com.sapo.mockprojectpossystem.product.interfaces.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BrandQueryParams {

    @NotBlank
    private int page = 0;

    @NotBlank
    private int size = 10;
}
