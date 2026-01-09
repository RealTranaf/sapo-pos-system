package com.sapo.mockprojectpossystem.customer.interfaces.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sapo.mockprojectpossystem.customer.domain.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@NoArgsConstructor
public class CreateCustomerRequest {
    public String name;
    public String phoneNum;
    public Gender gender;
    public String note;
}
