package com.sapo.mockprojectpossystem.service;

import com.sapo.mockprojectpossystem.model.Role;
import com.sapo.mockprojectpossystem.model.User;
import com.sapo.mockprojectpossystem.repository.UserRepository;
import com.sapo.mockprojectpossystem.response.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    private static final String PHONE_REGEX = "^0[2|3|5|7|8|9][0-9]{8,9}$";

    public LoginResponse auth(String username, String password){

        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username must not be empty");
        }
        if (password == null || password.isBlank()) {
            throw new RuntimeException("Password must not be empty");
        }

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

    public void signup(String username, String name, String phoneNum, String password, boolean isActive, Role role){
        validateUser(username, phoneNum, password, role);

        if (userRepository.existsByPhoneNum(phoneNum) || userRepository.existsByUsername(username)) {
            throw new RuntimeException("This phone number or username has already been registered");
        }

        User user = new User(username, name, phoneNum, passwordEncoder.encode(password), isActive, role);
        userRepository.save(user);
    }

    public static void validateUser(String username, String phoneNum, String password, Role role) {
        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username must not be empty");
        }
        if (username.trim().length() < 3 || username.trim().length() > 30)
            throw new RuntimeException("Username must be 3-30 characters");

        if (phoneNum == null || !phoneNum.matches(PHONE_REGEX)) {
            throw new RuntimeException("Phone number is invalid");
        }
        if (password == null || password.length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        if (role == null) {
            throw new RuntimeException("Role is required");
        }
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

    public void resetPassword(String phoneNum, String newPassword) {

        if (phoneNum == null || !phoneNum.matches(PHONE_REGEX)) {
            throw new RuntimeException("Invalid phone number");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }

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