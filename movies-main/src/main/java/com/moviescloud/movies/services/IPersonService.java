package com.moviescloud.movies.services;

import com.moviescloud.movies.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPersonService {
    Page<Person> findAll(Pageable pageable);

    Person findById(Long id);

    Person save(Person person);

    void delete(Person person);
}
