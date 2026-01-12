package com.sapo.mockprojectpossystem.product.interfaces.response;

import com.sapo.mockprojectpossystem.product.domain.model.Product;
import com.sapo.mockprojectpossystem.product.domain.model.Brand;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BrandLongResponse {
    private Integer id;
    private String name;
    private Instant createdOn;
    private Instant modifiedOn;
    private List<Integer> productsId;

    public BrandLongResponse(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.createdOn = brand.getCreatedOn();
        this.modifiedOn = brand.getModifiedOn();
        this.productsId = brand.getProducts()
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }
}
