package com.sapo.mockprojectpossystem.auth.interfaces.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    public String username;
    public String password;
}
