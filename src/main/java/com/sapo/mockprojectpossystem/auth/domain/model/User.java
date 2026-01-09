package com.sapo.mockprojectpossystem.auth.domain.model;

import com.sapo.mockprojectpossystem.auth.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 30)
    private String username;

    private String name;

    @Column(nullable = false, unique = true, length = 12)
    private String phoneNum;

    @Column(nullable = false)
    @Size(min = 6)
    private String password;

    private boolean isActive;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String name, String phoneNum, String password, boolean isActive, Role role) {
        this.username = username;
        this.name = name;
        this.phoneNum = phoneNum;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
    }

    private String token;

    private boolean expired;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
