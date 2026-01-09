package com.sapo.mockprojectpossystem.auth.interfaces.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserQueryParams {

    private String keyword;

    @NotBlank
    private int page = 0;

    @NotBlank
    private int size = 10;

    @NotBlank
    private String sortBy = "id";

    @NotBlank
    private String sortDir = "asc";
}
