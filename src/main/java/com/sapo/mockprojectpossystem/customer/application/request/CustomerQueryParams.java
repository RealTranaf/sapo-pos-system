package com.sapo.mockprojectpossystem.customer.application.request;

import com.sapo.mockprojectpossystem.customer.domain.model.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerQueryParams {
    private String keyword;

    @NotBlank
    private int page = 0;

    @NotBlank
    private int size = 10;

    private String startDate;
    private String endDate;

    private Double minAmount;
    private Double maxAmount;

    @NotBlank
    private String sortBy = "createdOn";

    @NotBlank
    private String sortDir = "desc";

    private Gender gender;
}
