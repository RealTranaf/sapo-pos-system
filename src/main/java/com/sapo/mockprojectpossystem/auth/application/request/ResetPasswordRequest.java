package com.sapo.mockprojectpossystem.auth.application.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequest {
    public String phoneNum;
    public String newPassword;
}
