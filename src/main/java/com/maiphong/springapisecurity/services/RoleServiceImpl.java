package com.maiphong.springapisecurity.services;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maiphong.springapisecurity.dtos.role.RoleCreateUpdateDTO;
import com.maiphong.springapisecurity.dtos.role.RoleDTO;
import com.maiphong.springapisecurity.entities.Role;
import com.maiphong.springapisecurity.repositories.RoleRepository;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository _roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this._roleRepository = roleRepository;
    }

    @Override
    public List<RoleDTO> findAll() {
        // Get all role entities
        var roles = _roleRepository.findAll();

        // Convert role entities to role DTOs

        var roleDTOs = roles.stream().map(role -> {
            var roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setName(role.getName());
            roleDTO.setDescription(role.getDescription());
            return roleDTO;
        }).toList();

        return roleDTOs;
    }

    @Override
    public RoleDTO findById(UUID id) {
        // Get role entity by id
        var role = _roleRepository.findById(id).orElse(null);

        // If role entity is null, return null
        if (role == null) {
            return null;
        }

        // Convert role entity to role DTO

        var roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());

        return roleDTO;
    }

    @Override
    public RoleDTO create(RoleCreateUpdateDTO roleCreateUpdateDTO) {
        // Check if role create update DTO is null
        if (roleCreateUpdateDTO == null) {
            throw new IllegalArgumentException("Role is required");
        }

        // Check role with the same name exists
        var existingRole = _roleRepository.findByName(roleCreateUpdateDTO.getName());

        if (existingRole != null) {
            throw new IllegalArgumentException("Role with the same name already exists");
        }

        var role = new Role();
        role.setName(roleCreateUpdateDTO.getName());
        role.setDescription(roleCreateUpdateDTO.getDescription());

        // Save role entity
        role = _roleRepository.save(role);

        // Convert role entity to role DTO
        var roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());

        return roleDTO;
    }

    @Override
    public RoleDTO update(UUID id, RoleCreateUpdateDTO roleCreateUpdateDTO) {
        // Check if role create update DTO is null
        if (roleCreateUpdateDTO == null) {
            throw new IllegalArgumentException("Role is required");
        }

        // Get role entity by id
        var role = _roleRepository.findById(id).orElse(null);
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }

        // Check role with the same name exists
        var existingRole = _roleRepository.findByName(roleCreateUpdateDTO.getName());

        if (existingRole != null && !existingRole.getId().equals(id)) {
            throw new IllegalArgumentException("Role with the same name already exists");
        }

        role.setName(roleCreateUpdateDTO.getName());
        role.setDescription(roleCreateUpdateDTO.getDescription());

        // Save role entity
        role = _roleRepository.save(role);

        // Convert role entity to role DTO
        var roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());

        return roleDTO;
    }

    @Override
    public boolean delete(UUID id) {
        var existingRole = _roleRepository.findById(id).orElse(null);

        if (existingRole == null) {
            throw new IllegalArgumentException("Role not found");
        }

        _roleRepository.delete(existingRole);

        // Check if role entity is deleted
        var isDeleted = _roleRepository.findById(id).isEmpty();

        return isDeleted;
    }
}
