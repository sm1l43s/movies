package com.moviescloud.movies.services;

import com.moviescloud.movies.entities.Profession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProfessionService {
    Page<Profession> findAll(Pageable pageable);
    Profession findById(long id);
    Profession save(Profession profession);
    void delete(Profession profession);
}
