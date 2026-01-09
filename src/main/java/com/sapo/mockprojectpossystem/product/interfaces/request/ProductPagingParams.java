package com.sapo.mockprojectpossystem.product.interfaces.request;

import com.sapo.mockprojectpossystem.common.annotation.ValidSortBy;
import com.sapo.mockprojectpossystem.product.domain.model.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductPagingParams {
    Integer page = 0;

    @Min(value = 20, message = "Limit must be between 20 and 50 include.")
    @Max(value = 50, message = "Limit must be between 20 and 50 include.")
    Integer limit = 20;

    @ValidSortBy(entity = Product.class)
    String sortBy = "modifiedOn";

    @Pattern(regexp = "asc|desc", message = "Order must be asc or desc.")
    String order = "asc";
}

