package com.sapo.mockprojectpossystem.common.validation;

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
        if (phoneNum == null || !phoneNum.matches(PHONE_REGEX)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.length() < 6) {
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
    }
}
