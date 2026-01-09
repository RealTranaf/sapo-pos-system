package com.sapo.mockprojectpossystem.product.interfaces.response;

import com.sapo.mockprojectpossystem.product.domain.model.Product;
import com.sapo.mockprojectpossystem.product.domain.model.Brand;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BrandLongResponse {
    private Integer id;
    private String name;
    private LocalDateTime createdAt;
    private List<Integer> productsId;

    public BrandLongResponse(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.createdAt = brand.getCreatedAt();
        this.productsId = brand.getProducts()
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }
}
