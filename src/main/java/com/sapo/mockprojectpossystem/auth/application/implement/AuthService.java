package com.sapo.mockprojectpossystem.auth.application.implement;

import com.sapo.mockprojectpossystem.auth.domain.model.Role;
import com.sapo.mockprojectpossystem.auth.domain.model.User;
import com.sapo.mockprojectpossystem.auth.domain.repository.UserRepository;
import com.sapo.mockprojectpossystem.auth.application.request.LoginRequest;
import com.sapo.mockprojectpossystem.auth.application.request.ResetPasswordRequest;
import com.sapo.mockprojectpossystem.auth.application.request.SignUpRequest;
import com.sapo.mockprojectpossystem.auth.application.response.LoginResponse;
import com.sapo.mockprojectpossystem.auth.application.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sapo.mockprojectpossystem.auth.validation.UserValidation.validateUser;
import static com.sapo.mockprojectpossystem.common.validation.CommonValidation.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public LoginResponse auth(LoginRequest request){
        String username = request.getUsername();
        String password = request.getPassword();

        validateUsername(username);
        validatePassword(password);

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

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

    public UserResponse signup(SignUpRequest request){
        String username = request.getUsername();
        String name = request.getName();
        String phoneNum = request.getPhoneNum();
        String password = request.getPassword();
        boolean isActive = request.isActive();
        Role role = request.getRole();

        validateUser(username, phoneNum, password, role);

        if (userRepository.existsByPhoneNum(request.getPhoneNum()) || userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("This phone number or username has already been registered");
        }

        User user = new User(username, name, phoneNum, passwordEncoder.encode(password), isActive, role);
        return new UserResponse(userRepository.save(user));
    }

    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (token.equals(user.getToken())) {
                user.setExpired(true);
                user.setToken(null);
                userRepository.save(user);
            }
        }
    }

    public void resetPassword(ResetPasswordRequest request) {
        String phoneNum = request.getPhoneNum();
        String newPassword = request.getNewPassword();

        validatePhone(phoneNum);
        validatePassword(newPassword);

        Optional<User> optionalUser = userRepository.findByPhoneNum(phoneNum);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
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