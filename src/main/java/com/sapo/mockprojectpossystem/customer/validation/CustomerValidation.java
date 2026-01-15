package com.sapo.mockprojectpossystem.customer.validation;

import com.sapo.mockprojectpossystem.customer.domain.model.Gender;
import lombok.NoArgsConstructor;

import static com.sapo.mockprojectpossystem.common.validation.CommonValidation.*;

@NoArgsConstructor
public class CustomerValidation {

    public static void validateCustomer(String name, String phoneNum, Gender gender) {
        validateName(name);
        validatePhone(phoneNum);
        validateGender(gender);
    }
}
