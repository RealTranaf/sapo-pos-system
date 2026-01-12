package com.sapo.mockprojectpossystem.product.interfaces.response;

import com.sapo.mockprojectpossystem.product.domain.model.Brand;
import com.sapo.mockprojectpossystem.product.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandResponse {
    private Integer id;
    private String name;

    public BrandResponse(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
    }
}