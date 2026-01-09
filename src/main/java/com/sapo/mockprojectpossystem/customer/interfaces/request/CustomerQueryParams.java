package com.sapo.mockprojectpossystem.customer.interfaces.request;

import com.sapo.mockprojectpossystem.customer.domain.enums.Gender;
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
    private String sortBy = "createdAt";

    @NotBlank
    private String sortDir = "desc";

    private Gender gender;
}
