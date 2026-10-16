package com.taskmanagement.config;

import com.taskmanagement.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class BeansConfig {
    private final UserRepository users;
    public BeansConfig(UserRepository users) { this.users = users; }
    @Bean UserDetailsService userDetailsService() {
        return username -> users.findByEmailIgnoreCase(username).orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found"));
    }
    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { return config.getAuthenticationManager(); }
}
