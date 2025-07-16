package com.hoangminh.controller.api.admin;

import com.hoangminh.dto.RoleDTO;
import com.hoangminh.entity.Role;
import com.hoangminh.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("")
    public List<RoleDTO> getAll() {
        return roleService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RoleDTO getById(@PathVariable Integer id) {
        Optional<Role> role = roleService.findById(id);
        return role.map(this::toDTO).orElse(null);
    }

    @PostMapping("")
    public RoleDTO create(@RequestBody RoleDTO dto) {
        Role entity = toEntity(dto);
        return toDTO(roleService.save(entity));
    }

    @PutMapping("/{id}")
    public RoleDTO update(@PathVariable Integer id, @RequestBody RoleDTO dto) {
        dto.setId(id);
        Role entity = toEntity(dto);
        return toDTO(roleService.save(entity));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        roleService.deleteById(id);
    }

    private RoleDTO toDTO(Role entity) {
        RoleDTO dto = new RoleDTO();
        dto.setId(entity.getId());
        dto.setTenRole(entity.getTenRole());
        dto.setMoTa(entity.getMoTa());
        return dto;
    }

    private Role toEntity(RoleDTO dto) {
        Role entity = new Role();
        entity.setId(dto.getId());
        entity.setTenRole(dto.getTenRole());
        entity.setMoTa(dto.getMoTa());
        return entity;
    }
}