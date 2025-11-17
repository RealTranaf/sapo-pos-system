package com.sapo.mockprojectpossystem.controller;

import com.sapo.mockprojectpossystem.response.LoginResponse;
import com.sapo.mockprojectpossystem.response.MessageResponse;
import com.sapo.mockprojectpossystem.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    record AuthRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest){
        try{
            LoginResponse loginResponse = authService.auth(authRequest.username, authRequest.password);
            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}