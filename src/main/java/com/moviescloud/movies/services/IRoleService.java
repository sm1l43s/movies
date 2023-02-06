package com.moviescloud.movies.services;

import com.moviescloud.movies.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoleService {
    Page<Role> findAll(Pageable pageable);
    Role findById(Long id);
    Role findByName(String name);
    Role save(Role role);
    void delete(Role role);
}
