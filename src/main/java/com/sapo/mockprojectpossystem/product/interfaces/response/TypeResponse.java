package com.sapo.mockprojectpossystem.product.interfaces.response;

import com.sapo.mockprojectpossystem.product.domain.model.Type;
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

    public TypeResponse(Type type) {
        this.id = type.getId();
        this.name = type.getName();
    }
}