package com.sapo.mockprojectpossystem.auth.interfaces.rest;

import com.sapo.mockprojectpossystem.auth.domain.enums.Role;
import com.sapo.mockprojectpossystem.auth.domain.model.User;
import com.sapo.mockprojectpossystem.auth.interfaces.request.UpdateUserRequest;
import com.sapo.mockprojectpossystem.auth.interfaces.request.UserQueryParams;
import com.sapo.mockprojectpossystem.auth.interfaces.response.UserResponse;
import com.sapo.mockprojectpossystem.common.response.MessageResponse;
import com.sapo.mockprojectpossystem.auth.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                return ResponseEntity.status(401).body(new MessageResponse("Unauthorized"));
            }

            String username = auth.getName();
            return ResponseEntity.ok(userService.getUserByUsername(username));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Lấy danh sách user, có sorting và tìm kiếm
    @PreAuthorize("hasAnyRole('OWNER')")
    @GetMapping
    public ResponseEntity<?> getAllUser(@ModelAttribute UserQueryParams query) {
        try {
            return ResponseEntity.ok(userService.getAllUser(query));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Lấy user theo id
    @PreAuthorize("hasAnyRole('OWNER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Cập nhật user
    @PreAuthorize("hasAnyRole('OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,
                                        @ModelAttribute UpdateUserRequest request) {
        try {
            userService.updateUser(id, request);
            return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

}