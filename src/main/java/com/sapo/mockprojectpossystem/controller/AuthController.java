package com.sapo.mockprojectpossystem.controller;

import com.sapo.mockprojectpossystem.model.Role;
import com.sapo.mockprojectpossystem.response.LoginResponse;
import com.sapo.mockprojectpossystem.response.MessageResponse;
import com.sapo.mockprojectpossystem.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
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

    record LoginRequest(String username, String password) {}

    record SignUpRequest(String username, String name, String phoneNum, String password, Role role, boolean isActive) {}

    record ResetRequest(String phoneNum, String newPassword) {}

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest){
        try{
            LoginResponse loginResponse = authService.auth(loginRequest.username, loginRequest.password);
            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Tạo tài khoản (dành cho OWNER)
    @PostMapping("/signup")
    public ResponseEntity<?> createAccount(@RequestBody SignUpRequest signUpRequest){
        try{
            authService.signup(signUpRequest.username, signUpRequest.name, signUpRequest.phoneNum, signUpRequest.password, signUpRequest.isActive, signUpRequest.role);
            return ResponseEntity.ok().body(new MessageResponse("Account created successfully"));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Đăng xuất
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            authService.logout(authHeader);
            return ResponseEntity.ok("Logged out successfully");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Reset mật khẩu
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetRequest resetRequest) {
        try {
            authService.resetPassword(resetRequest.phoneNum, resetRequest.newPassword);
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}