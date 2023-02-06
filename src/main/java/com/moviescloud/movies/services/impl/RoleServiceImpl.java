package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Role;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repositories.RoleRepository;
import com.moviescloud.movies.services.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleServiceImpl implements IRoleService {

    final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        log.info("Get a list of roles");
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role findById(Long id) {
        log.info("Get a role by id{}", id);
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role with id=" + id + " not found"));
    }

    @Override
    public Role findByName(String name) {
        log.info("Get a role by name{}", name);
        return roleRepository.findByRoleName(name).orElseThrow(() -> new ResourceNotFoundException("Role with name=" + name + " not found"));
    }

    @Override
    public Role save(Role role) {
        log.info("Save a new role{}", role);
        return roleRepository.save(role);
    }

    @Override
    public void delete(Role role) {
        log.info("Delete a role{}", role);
        roleRepository.delete(role);
    }
}
