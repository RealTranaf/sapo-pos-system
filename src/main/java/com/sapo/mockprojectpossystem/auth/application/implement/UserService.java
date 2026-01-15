package com.sapo.mockprojectpossystem.auth.application.implement;

import com.sapo.mockprojectpossystem.auth.domain.model.User;
import com.sapo.mockprojectpossystem.auth.domain.repository.UserRepository;
import com.sapo.mockprojectpossystem.auth.application.request.UpdateUserRequest;
import com.sapo.mockprojectpossystem.auth.application.request.UserQueryParams;
import com.sapo.mockprojectpossystem.auth.application.response.UserResponse;
import com.sapo.mockprojectpossystem.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sapo.mockprojectpossystem.auth.validation.UserValidation.validateUserNoPassword;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // Quang
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User doesn't exist"));
        return new UserResponse(user);
    }

    public PageResponse<UserResponse> getAllUser(UserQueryParams query) {
        Sort.Direction direction = query.getSortDir().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), Sort.by(direction, query.getSortBy()));
        Page<UserResponse> page = userRepository.searchUser(query.getKeyword().trim(), pageable).map(UserResponse::new);
        return new PageResponse<UserResponse>("users", page);
    }

    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User doesn't exist"));
        return new UserResponse(user);
    }

    public UserResponse updateUser(Integer id, UpdateUserRequest request) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isEmpty()){
            throw new RuntimeException("User doesn't exist");
        }
        User user = optional.get();
        validateUserNoPassword(request.getUsername(), request.getPhoneNum(), request.getRole());
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setPhoneNum(request.getPhoneNum());
        user.setRole(request.getRole());
        user.setActive(request.getIsActive());
        return new UserResponse(userRepository.save(user));
    }
}
