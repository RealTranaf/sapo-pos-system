package com.sapo.mockprojectpossystem.auth.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    SALES,
    CS,
    WAREHOUSE,
    OWNER
}
