package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Privilege;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repositories.PrivilegeRepository;
import com.moviescloud.movies.services.IPrivilegeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrivilegeServiceImpl implements IPrivilegeService {

    final PrivilegeRepository privilegeRepository;

    @Autowired
    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    public Page<Privilege> findAll(Pageable pageable) {
        log.info("Get a list of privileges");
        return privilegeRepository.findAll(pageable);
    }

    @Override
    public Privilege findById(Long id) {
        log.info("Get a privilege by id{}", id);
        return privilegeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Privilege with id=" + id + " not found"));
    }

    @Override
    public Privilege findByName(String name) {
        log.info("Get a privilege by name{}", name);
        return privilegeRepository.findByPrivilegeName(name).orElseThrow(() -> new ResourceNotFoundException("Privilege with name=" + name + " not found"));
    }

    @Override
    public Privilege save(Privilege privilege) {
        log.info("Save a new privilege{}", privilege);
        return privilegeRepository.save(privilege);
    }

    @Override
    public void delete(Privilege privilege) {
        log.info("Delete a privilege{}", privilege);
        privilegeRepository.delete(privilege);
    }
}
