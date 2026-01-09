package com.sapo.mockprojectpossystem.purchase.validation;

import lombok.NoArgsConstructor;

import static com.sapo.mockprojectpossystem.common.validation.CommonValidation.validateName;
import static com.sapo.mockprojectpossystem.common.validation.CommonValidation.validatePhone;

@NoArgsConstructor
public class PurchaseValidation {

    public static void validatePurchase(String name, String phoneNum) {
        validateName(name);
        validatePhone(phoneNum);
    }
}
