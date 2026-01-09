package com.sapo.mockprojectpossystem.product.interfaces.response;

import com.sapo.mockprojectpossystem.product.domain.model.Product;
import com.sapo.mockprojectpossystem.product.domain.model.Type;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TypeLongResponse {
    private Integer id;
    private String name;
    private LocalDateTime createdAt;
    private List<Integer> productsId;

    public TypeLongResponse(Type type) {
        this.id = type.getId();
        this.name = type.getName();
        this.createdAt = type.getCreatedAt();
        this.productsId = type.getProducts().stream().map(Product::getId).toList();
    }
}
