package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Country;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repositories.CountryRepository;
import com.moviescloud.movies.services.ICountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CountryServiceImpl implements ICountryService {

    final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Page<Country> findAll(Pageable pageable) {
        log.info("Getting a list of countries");
        return countryRepository.findAll(pageable);
    }

    @Override
    public Country findById(Long id) {
        log.info("Getting a country by id{}", id);
        return countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country with id=" + id + " not found"));
    }

    @Override
    public Country save(Country country) {
        log.info("Save country{}", country);
        return countryRepository.save(country);
    }

    @Override
    public void delete(Country country) {
        log.info("Delete country{}", country);
        countryRepository.delete(country);
    }
}
