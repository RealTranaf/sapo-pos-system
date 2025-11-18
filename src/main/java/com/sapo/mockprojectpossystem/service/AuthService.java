package com.sapo.mockprojectpossystem.service;

import com.sapo.mockprojectpossystem.model.User;
import com.sapo.mockprojectpossystem.repository.UserRepository;
import com.sapo.mockprojectpossystem.response.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public LoginResponse auth(String username, String password){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
        expireOldToken(user);
        String jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return new LoginResponse(user.getUsername(), user.getRole(), jwtToken, jwtService.getJwtExpiration());
    }

    public void expireOldToken(User user){
        if (user.getToken() != null) {
            user.setExpired(true);
            userRepository.save(user);
        }
    }

    public void saveUserToken(User user, String jwtToken){
        user.setToken(jwtToken);
        user.setExpired(false);
        userRepository.save(user);
    }
}