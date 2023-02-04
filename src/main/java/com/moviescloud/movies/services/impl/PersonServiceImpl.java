package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Person;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repositories.PersonRepository;
import com.moviescloud.movies.services.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PersonServiceImpl implements IPersonService {

    PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
        log.info("Getting a list of persons");
        return personRepository.findAll(pageable);
    }

    @Override
    public Person findById(Long id) {
        log.info("Getting a person by id={}", id);
        return personRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Person with id="+ id + " not found"));
    }

    @Override
    public Person save(Person person) {
        log.info("Save person={}", person);
        return personRepository.save(person);
    }

    @Override
    public void delete(Person person) {
        log.info("Delete person={}", person);
        personRepository.delete(person);
    }
}
