package com.sapo.mockprojectpossystem.auth.interfaces.request;

import com.sapo.mockprojectpossystem.auth.domain.enums.Role;
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
