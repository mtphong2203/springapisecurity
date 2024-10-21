package com.maiphong.springapisecurity.services;

import java.util.List;
import java.util.UUID;

import com.maiphong.springapisecurity.dtos.user.UserCreateDTO;
import com.maiphong.springapisecurity.dtos.user.UserDTO;
import com.maiphong.springapisecurity.dtos.user.UserUpdateDTO;

public interface UserService {
    List<UserDTO> findAll();

    UserDTO findById(UUID id);

    UserDTO create(UserCreateDTO userCreateDTO);

    UserDTO update(UUID id, UserUpdateDTO userCreateDTO);

    boolean delete(UUID id);
}
