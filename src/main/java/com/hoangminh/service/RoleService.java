package com.hoangminh.service;

import com.hoangminh.entity.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();

    Optional<Role> findById(Integer id);

    Role save(Role role);

    void deleteById(Integer id);
}