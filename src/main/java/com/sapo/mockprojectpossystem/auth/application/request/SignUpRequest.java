package com.sapo.mockprojectpossystem.auth.application.request;

import com.sapo.mockprojectpossystem.auth.domain.model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {
    public String username;
    public String name;
    public String phoneNum;
    public String password;
    public Role role;
    public boolean isActive;
}
