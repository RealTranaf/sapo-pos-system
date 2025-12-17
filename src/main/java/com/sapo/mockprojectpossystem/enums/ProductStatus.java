package com.sapo.mockprojectpossystem.enums;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true)
public enum ProductStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    String value;

    ProductStatus(String value) {
        this.value = value;
    }
}
