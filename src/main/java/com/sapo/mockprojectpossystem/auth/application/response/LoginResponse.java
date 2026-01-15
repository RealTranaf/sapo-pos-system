package com.sapo.mockprojectpossystem.auth.application.response;

import com.sapo.mockprojectpossystem.auth.domain.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String username;
    private Role role;
    private String token;
    private Long expiresIn;

    public LoginResponse(String username, Role role, String token, Long expiresIn) {
        this.username = username;
        this.role = role;
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
