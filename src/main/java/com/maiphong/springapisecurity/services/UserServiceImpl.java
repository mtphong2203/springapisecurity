package com.maiphong.springapisecurity.services;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maiphong.springapisecurity.dtos.user.UserCreateDTO;
import com.maiphong.springapisecurity.dtos.user.UserDTO;
import com.maiphong.springapisecurity.dtos.user.UserUpdateDTO;
import com.maiphong.springapisecurity.entities.User;
import com.maiphong.springapisecurity.repositories.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository _userRepository;
    private final PasswordEncoder _passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this._userRepository = userRepository;
        this._passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> findAll() {
        // Get all user entities
        var users = _userRepository.findAll();

        // Convert user entities to user DTOs

        var userDTOs = users.stream().map(user -> {
            var userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            return userDTO;
        }).toList();

        return userDTOs;
    }

    @Override
    public UserDTO findById(UUID id) {
        // Get user entity by id
        var user = _userRepository.findById(id).orElse(null);

        // If user entity is null, return null
        if (user == null) {
            return null;
        }

        // Convert user entity to user DTO

        var userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }

    @Override
    public UserDTO create(UserCreateDTO userCreateDTO) {
        // Check if user create update DTO is null
        if (userCreateDTO == null) {
            throw new IllegalArgumentException("User is required");
        }

        // Check user with the same name exists
        var existingUser = _userRepository.findByUsername(userCreateDTO.getUsername());

        if (existingUser != null) {
            throw new IllegalArgumentException("User with the same name already exists");
        }

        var user = new User();
        user.setFirstName(userCreateDTO.getFirstName());
        user.setLastName(userCreateDTO.getLastName());
        user.setPhoneNumber(userCreateDTO.getPhoneNumber());
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        // Set hashed password
        user.setPassword(_passwordEncoder.encode(userCreateDTO.getPassword()));

        // Save user entity
        user = _userRepository.save(user);

        // Convert user entity to user DTO
        var userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }

    @Override
    public UserDTO update(UUID id, UserUpdateDTO userUpdateDTO) {
        // Check if user create update DTO is null
        if (userUpdateDTO == null) {
            throw new IllegalArgumentException("User is required");
        }

        // Get user entity by id
        var user = _userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Check user with the same email exists
        var existingUser = _userRepository.findByEmail(userUpdateDTO.getEmail());

        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new IllegalArgumentException("User with the same email already exists");
        }

        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        user.setEmail(userUpdateDTO.getEmail());

        // Save user entity
        user = _userRepository.save(user);

        // Convert user entity to user DTO
        var userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }

    @Override
    public boolean delete(UUID id) {
        var existingUser = _userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        _userRepository.delete(existingUser);

        // Check if user entity is deleted
        var isDeleted = _userRepository.findById(id).isEmpty();

        return isDeleted;
    }
}
