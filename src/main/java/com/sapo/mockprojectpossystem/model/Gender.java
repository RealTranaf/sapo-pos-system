package com.sapo.mockprojectpossystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE,
    FEMALE,
    NaN
}
