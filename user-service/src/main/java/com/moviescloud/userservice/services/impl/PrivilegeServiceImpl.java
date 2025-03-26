package com.moviescloud.userservice.services.impl;

import com.moviescloud.common.entities.Privilege;
import com.moviescloud.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.moviescloud.userservice.repositories.PrivilegeRepository;
import com.moviescloud.userservice.services.IPrivilegeService;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements IPrivilegeService {

    final PrivilegeRepository privilegeRepository;

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
