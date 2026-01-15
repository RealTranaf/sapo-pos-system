package com.sapo.mockprojectpossystem.purchase.application.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseQueryParams {
    private String keyword;

    private Integer customerId;

    private Integer userId;

    private Double minTotal;

    private Double maxTotal;

    private Double minDiscount;

    private Double maxDiscount;

    private String startDate;

    private String endDate;

    private Integer productId;

    @NotBlank
    private int page = 0;

    @NotBlank
    private int size = 10;

    @NotBlank
    private String sortBy = "createdOn";

    @NotBlank
    private String sortDir = "desc";
}
