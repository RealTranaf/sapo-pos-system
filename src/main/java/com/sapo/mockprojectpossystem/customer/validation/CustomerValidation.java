package com.sapo.mockprojectpossystem.customer.validation;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomerValidation {
    private static final String PHONE_REGEX = "^0[2|3|5|7|8|9][0-9]{8,9}$";

    public static void validateCreate(
            String name,
            String phoneNum
    ) {
        validateName(name);
        validatePhone(phoneNum);
    }

    public static void validateUpdate(
            String name,
            String phoneNum
    ) {
        validateName(name);
        validatePhone(phoneNum);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (name.length() < 2 || name.length() > 40) {
            throw new IllegalArgumentException("Name must be 2–40 characters");
        }
    }

    private static void validatePhone(String phoneNum) {
        if (phoneNum == null || !phoneNum.matches(PHONE_REGEX)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

}
