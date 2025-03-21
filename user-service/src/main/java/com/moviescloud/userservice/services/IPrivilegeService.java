package com.moviescloud.userservice.services;

import com.moviescloud.common.entities.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPrivilegeService {
    Page<Privilege> findAll(Pageable pageable);

    Privilege findById(Long id);

    Privilege findByName(String name);

    Privilege save(Privilege privilege);

    void delete(Privilege privilege);
}
