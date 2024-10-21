package com.maiphong.springapisecurity.services;

import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maiphong.springapisecurity.dtos.auth.RegisterRequestDTO;
import com.maiphong.springapisecurity.entities.User;
import com.maiphong.springapisecurity.repositories.UserRepository;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Trien khai login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by username and password
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    @Override
    public Boolean register(RegisterRequestDTO registerRequestDTO) {
        var existingUser = userRepository.findByUsername(registerRequestDTO.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Compare password and confirm password
        if (!registerRequestDTO.getPassword().equals(registerRequestDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        var user = new User();
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setPhoneNumber(registerRequestDTO.getPhoneNumber());
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        userRepository.save(user);

        return user != null;
    }
}
