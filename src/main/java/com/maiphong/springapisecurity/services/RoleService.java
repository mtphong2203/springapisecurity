package com.maiphong.springapisecurity.services;

import java.util.List;
import java.util.UUID;

import com.maiphong.springapisecurity.dtos.role.RoleCreateUpdateDTO;
import com.maiphong.springapisecurity.dtos.role.RoleDTO;

public interface RoleService {
    List<RoleDTO> findAll();

    RoleDTO findById(UUID id);

    RoleDTO create(RoleCreateUpdateDTO roleCreateUpdateDTO);

    RoleDTO update(UUID id, RoleCreateUpdateDTO roleCreateUpdateDTO);

    boolean delete(UUID id);
}
