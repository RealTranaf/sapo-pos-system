package com.sapo.mockprojectpossystem;

import com.sapo.mockprojectpossystem.model.Role;
import com.sapo.mockprojectpossystem.model.User;
import com.sapo.mockprojectpossystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MockProjectPosSystemApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MockProjectPosSystemApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(MockProjectPosSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("Owner").isEmpty()) {
            User user = new User("Owner", "0123", passwordEncoder.encode("admin"), Role.OWNER);
            userRepository.save(user);
        }
    }
}
