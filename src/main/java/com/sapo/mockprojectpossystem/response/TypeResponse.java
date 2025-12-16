package com.sapo.mockprojectpossystem.response;

import com.sapo.mockprojectpossystem.model.Product;
import com.sapo.mockprojectpossystem.model.Type;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TypeResponse {
    private Integer id;
    private String name;
    private LocalDateTime createdAt;
    private List<Long> productsId;

    public TypeResponse(Type type) {
        this.id = type.getId();
        this.name = type.getName();
        this.createdAt = type.getCreatedAt();
        this.productsId = type.getProducts().stream().map(Product::getId).toList();
    }
}
