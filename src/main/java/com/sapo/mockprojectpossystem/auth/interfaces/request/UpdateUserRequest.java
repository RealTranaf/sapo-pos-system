package com.sapo.mockprojectpossystem.auth.interfaces.request;

import com.sapo.mockprojectpossystem.auth.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNum;

    @NotBlank
    private Boolean isActive;

    @NotBlank
    private Role role;
}
