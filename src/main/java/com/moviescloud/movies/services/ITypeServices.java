package com.moviescloud.movies.services;

import com.moviescloud.movies.entities.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITypeServices {
    Page<Type> findAll(Pageable pageable);

    Type findById(Long id);
}
