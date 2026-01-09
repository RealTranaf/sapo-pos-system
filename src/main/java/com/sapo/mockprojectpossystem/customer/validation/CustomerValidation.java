package com.sapo.mockprojectpossystem.customer.validation;

import lombok.NoArgsConstructor;

import static com.sapo.mockprojectpossystem.common.validation.CommonValidation.validateName;
import static com.sapo.mockprojectpossystem.common.validation.CommonValidation.validatePhone;

@NoArgsConstructor
public class CustomerValidation {

    public static void validateCustomer(String name, String phoneNum) {
        validateName(name);
        validatePhone(phoneNum);
    }
}
