package com.sapo.mockprojectpossystem.common.validation;

import com.sapo.mockprojectpossystem.auth.domain.model.Role;
import com.sapo.mockprojectpossystem.customer.domain.model.Gender;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommonValidation {

    private static final String PHONE_REGEX = "^0[2|3|5|7|8|9][0-9]{8,9}$";

    public static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (name.length() < 2 || name.length() > 40) {
            throw new IllegalArgumentException("Name must be 2–40 characters");
        }
    }

    public static void validatePhone(String phoneNum) {
        if (phoneNum == null || phoneNum.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (!phoneNum.matches(PHONE_REGEX)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
    }

    public static void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be empty");
        }
        if (username.trim().length() < 3 || username.trim().length() > 30){
            throw new IllegalArgumentException("Username must be 3-30 characters");
        }
    }

    public static void validateRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }
        if (!role.equals(Role.OWNER) && !role.equals(Role.CS) && !role.equals(Role.SALES) && !role.equals(Role.WAREHOUSE)) {
            throw new IllegalArgumentException("Invalid role");
        }
    }

    public static void validateGender(Gender gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Gender is required");
        }
        if (!gender.equals(Gender.NaN) && !gender.equals(Gender.MALE) && !gender.equals(Gender.FEMALE)) {
            throw new IllegalArgumentException("Invalid gender");
        }
    }

    public static void validatePaging(Integer page, Integer size) {
        if (page == null || page < 0) {
            throw new IllegalArgumentException("Page must be >= 0");
        }
        if (size == null || size <= 0 || size > 100) {
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }
    }
}
