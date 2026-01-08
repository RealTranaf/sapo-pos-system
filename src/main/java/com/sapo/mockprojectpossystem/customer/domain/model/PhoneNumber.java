package com.sapo.mockprojectpossystem.customer.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
public class PhoneNumber {

    @Column(name = "phone_num", nullable = false, length = 12)
    private String value;

    public PhoneNumber(String value) {
        this.value = value;
    }
}
