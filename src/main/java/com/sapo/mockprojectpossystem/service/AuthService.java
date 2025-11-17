package com.sapo.mockprojectpossystem.service;

import com.sapo.mockprojectpossystem.model.Token;
import com.sapo.mockprojectpossystem.model.User;
import com.sapo.mockprojectpossystem.repository.TokenRepository;
import com.sapo.mockprojectpossystem.repository.UserRepository;
import com.sapo.mockprojectpossystem.response.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService, UserRepository userRepository, TokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
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
        revokeAllTokenFromUser(user);
        String jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return new LoginResponse(user.getUsername(), user.getRole(), jwtToken, jwtService.getJwtExpiration());
    }

    public void revokeAllTokenFromUser(User user){
        List<Token> validUserToken = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserToken.isEmpty()){
            return;
        }
        validUserToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

    public void saveUserToken(User user, String jwttoken){
        Token token = Token.builder()
                .user(user)
                .token(jwttoken)
                .tokenType("BEARER")
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}