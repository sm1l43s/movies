package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Type;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repositories.TypeRepository;
import com.moviescloud.movies.services.ITypeServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeServiceImpl implements ITypeServices {

    final TypeRepository typeRepository;

    @Override
    public Page<Type> findAll(Pageable pageable) {
        log.info("Getting a list of type movies");
        return typeRepository.findAll(pageable);
    }

    @Override
    public Type findById(Long id) {
        log.info("Getting a list of type movie by id{}", id);
        return typeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Type movie with id = " + id + " not found"));
    }
}
