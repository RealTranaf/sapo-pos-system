package com.sapo.mockprojectpossystem.customer.application.request;

import com.sapo.mockprojectpossystem.customer.domain.model.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCustomerRequest {
    public String name;
    public String phoneNum;
    public Gender gender;
    public String note;
}
