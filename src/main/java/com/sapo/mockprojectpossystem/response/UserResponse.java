package com.sapo.mockprojectpossystem.response;

import com.sapo.mockprojectpossystem.model.Role;
import com.sapo.mockprojectpossystem.model.User;
import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String username;
    private String name;
    private String phoneNum;
    private boolean isActive;
    private Role role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.phoneNum = user.getPhoneNum();
        this.isActive = user.isActive();
        this.role = user.getRole();
    }
}
