package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Profession;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repositories.ProfessionRepository;
import com.moviescloud.movies.services.IProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProfessionServiceImpl implements IProfessionService {

    final ProfessionRepository professionRepository;

    @Autowired
    public ProfessionServiceImpl(ProfessionRepository professionRepository) {
        this.professionRepository = professionRepository;
    }

    @Override
    public Page<Profession> findAll(Pageable pageable) {
        return professionRepository.findAll(pageable);
    }

    @Override
    public Profession findById(long id) {
        return professionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Profession with id=" + id + " not found"));
    }

    @Override
    public Profession save(Profession profession) {
        return professionRepository.save(profession);
    }

    @Override
    public void delete(Profession profession) {
        professionRepository.delete(profession);
    }
}
