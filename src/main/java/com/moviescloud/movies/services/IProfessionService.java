package com.moviescloud.movies.services;

import com.moviescloud.movies.entities.Profession;
import org.springframework.data.domain.Pageable;

public interface IProfessionService {
    Iterable<Profession> findAll(Pageable pageable);
    Profession findById(long id);
    Profession save(Profession profession);
    void delete(Profession profession);
}
