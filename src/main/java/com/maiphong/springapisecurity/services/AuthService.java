package com.maiphong.springapisecurity.services;

import com.maiphong.springapisecurity.dtos.auth.RegisterRequestDTO;

public interface AuthService {
    Boolean register(RegisterRequestDTO registerRequestDTO);
}
