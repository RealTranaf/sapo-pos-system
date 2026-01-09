package com.sapo.mockprojectpossystem.product.interfaces.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeResponse {
    private Integer id;
    private String name;
}