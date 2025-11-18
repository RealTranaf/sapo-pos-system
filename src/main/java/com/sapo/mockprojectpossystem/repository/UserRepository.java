package com.sapo.mockprojectpossystem.repository;

import com.sapo.mockprojectpossystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneNum(String phoneNum);

    boolean existsByPhoneNum(String phoneNum);

    boolean existsByUsername(String username);

}
