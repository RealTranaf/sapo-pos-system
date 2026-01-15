package com.sapo.mockprojectpossystem.auth.application.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    public String username;
    public String password;
}
