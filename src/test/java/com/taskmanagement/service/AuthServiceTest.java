package com.taskmanagement.service;

import com.taskmanagement.dto.RegisterRequest;
import com.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthServiceTest {
    @Autowired AuthService authService;
    @Autowired UserRepository users;
    @Autowired PasswordEncoder encoder;

    @Test
    void registrationStoresAHashedUserPassword() {
        authService.register(new RegisterRequest("Test User", "test@example.com", "password123"));
        var user = users.findByEmailIgnoreCase("test@example.com").orElseThrow();
        assertThat(user.getPassword()).isNotEqualTo("password123");
        assertThat(encoder.matches("password123", user.getPassword())).isTrue();
    }
}
