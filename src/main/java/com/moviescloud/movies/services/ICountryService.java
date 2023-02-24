package com.moviescloud.movies.services;

import com.moviescloud.movies.entities.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICountryService {
    Page<Country> findAll(Pageable pageable);

    Country findById(Long id);

    Country save(Country country);

    void delete(Country country);
}
