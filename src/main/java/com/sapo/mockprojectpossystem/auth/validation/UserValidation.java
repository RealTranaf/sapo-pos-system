package com.sapo.mockprojectpossystem.auth.validation;

import com.sapo.mockprojectpossystem.auth.domain.model.Role;
import lombok.NoArgsConstructor;

import static com.sapo.mockprojectpossystem.common.validation.CommonValidation.*;

@NoArgsConstructor
public class UserValidation {

    public static void validateUser(String username, String phoneNum, String password, Role role) {
        validateUsername(username);
        validatePhone(phoneNum);
        validatePassword(password);
        validateRole(role.name());
    }

    public static void validateUserNoPassword(String username, String phoneNum, Role role) {
        validateUsername(username);
        validatePhone(phoneNum);
        validateRole(role.name());
    }
}
