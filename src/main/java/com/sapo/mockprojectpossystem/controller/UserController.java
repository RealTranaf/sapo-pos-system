package com.sapo.mockprojectpossystem.controller;

import com.sapo.mockprojectpossystem.model.Customer;
import com.sapo.mockprojectpossystem.model.Gender;
import com.sapo.mockprojectpossystem.model.Role;
import com.sapo.mockprojectpossystem.model.User;
import com.sapo.mockprojectpossystem.response.CustomerResponse;
import com.sapo.mockprojectpossystem.response.MessageResponse;
import com.sapo.mockprojectpossystem.response.UserResponse;
import com.sapo.mockprojectpossystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    // Quang
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                return ResponseEntity.status(401).body(new MessageResponse("Unauthorized"));
            }

            String username = auth.getName();
            User user = userService.getUserByUsername(username);

            return ResponseEntity.ok(new UserResponse(user));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllCustomers(@RequestParam(required = false) String keyword,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Map<String, Object> response = new HashMap<>();
            Page<User> userPage = userService.getAllUser(keyword, page, size, sortBy, sortDir);
            List<UserResponse> userResponses = userPage.stream().map(UserResponse::new).collect(Collectors.toList());
            response.put("users", userResponses);
            response.put("currentPage", userPage.getNumber());
            response.put("totalItems", userPage.getTotalElements());
            response.put("totalPages", userPage.getTotalPages());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(new UserResponse(user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,
                                        @RequestParam String username,
                                        @RequestParam String name,
                                        @RequestParam String phoneNum,
                                        @RequestParam boolean isActive,
                                        @RequestParam Role role) {
        try {
            userService.updateUser(id, username, name,  phoneNum, isActive, role);
            return ResponseEntity.ok("User updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

}
