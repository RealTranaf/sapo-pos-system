package com.sapo.mockprojectpossystem.customer.interfaces;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sapo.mockprojectpossystem.customer.domain.model.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@JsonRootName("customer")
public class CreateCustomerRequest {
    public String name;
    public String phoneNum;
    public Gender gender;
    public String note;
}
