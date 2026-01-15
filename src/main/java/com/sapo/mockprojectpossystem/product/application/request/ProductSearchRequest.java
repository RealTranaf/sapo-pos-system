package com.sapo.mockprojectpossystem.product.application.request;

import com.sapo.mockprojectpossystem.common.annotation.ValidSortBy;
import com.sapo.mockprojectpossystem.product.domain.model.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchRequest {
    Integer page = 0;

    @Min(value = 20, message = "Limit must be between 20 and 50 include.")
    @Max(value = 50, message = "Limit must be between 20 and 50 include.")
    Integer limit = 20;

    @ValidSortBy(entity = Product.class)
    String sortBy = "modifiedOn";

    @Pattern(regexp = "asc|desc", message = "Order must be asc or desc.")
    String order = "asc";

    private String search;

    private Integer brandId;

    private List<Integer> typeIds;
}
