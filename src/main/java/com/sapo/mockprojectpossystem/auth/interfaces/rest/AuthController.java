package com.sapo.mockprojectpossystem.auth.interfaces.rest;

import com.sapo.mockprojectpossystem.auth.interfaces.request.LoginRequest;
import com.sapo.mockprojectpossystem.auth.interfaces.request.ResetPasswordRequest;
import com.sapo.mockprojectpossystem.auth.interfaces.request.SignUpRequest;
import com.sapo.mockprojectpossystem.auth.interfaces.response.LoginResponse;
import com.sapo.mockprojectpossystem.common.response.MessageResponse;
import com.sapo.mockprojectpossystem.auth.application.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest request){
        try{
            return ResponseEntity.ok(authService.auth(request));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Tạo tài khoản (dành cho OWNER)
    @PostMapping("/signup")
    public ResponseEntity<?> createAccount(@RequestBody SignUpRequest request){
        try{
            authService.signup(request);
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
            return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Reset mật khẩu
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request);
            return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}