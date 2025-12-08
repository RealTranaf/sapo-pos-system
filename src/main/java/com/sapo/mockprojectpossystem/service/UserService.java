package com.sapo.mockprojectpossystem.service;

import com.sapo.mockprojectpossystem.model.Customer;
import com.sapo.mockprojectpossystem.model.Role;
import com.sapo.mockprojectpossystem.model.User;
import com.sapo.mockprojectpossystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<User> getAllUser(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return userRepository.searchUser(keyword.trim(), pageable);
    }

    public User getUserById(Integer id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("User doesn't exist");
        }
    }

    public void updateUser(Integer id, String username, String name, String phoneNum, boolean isActive, Role role) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isEmpty()){
            throw new RuntimeException("User doesn't exist");
        }
        User user = optional.get();
        AuthService.validateUser(username, phoneNum, "0987123456", role);
        user.setUsername(username);
        user.setName(name);
        user.setPhoneNum(phoneNum);
        user.setRole(role);
        user.setActive(isActive);
        userRepository.save(user);
    }
}
