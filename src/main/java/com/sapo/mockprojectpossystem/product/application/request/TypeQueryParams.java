package com.sapo.mockprojectpossystem.product.application.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TypeQueryParams {

    @NotBlank
    private int page = 0;

    @NotBlank
    private int size = 10;
}
