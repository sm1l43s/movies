package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Profession;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repositories.ProfessionRepository;
import com.moviescloud.movies.services.IProfessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessionServiceImpl implements IProfessionService {

    final ProfessionRepository professionRepository;

    @Override
    public Page<Profession> findAll(Pageable pageable) {
        log.info("Getting a list of profession");
        return professionRepository.findAll(pageable);
    }

    @Override
    public Profession findById(long id) {
        log.info("Getting profession by id{}", id);
        return professionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Profession with id=" + id + " not found"));
    }

    @Override
    public Profession save(Profession profession) {
        log.info("Save profession{}", profession);
        return professionRepository.save(profession);
    }

    @Override
    public void delete(Profession profession) {
        log.info("Delete profession{}", profession);
        professionRepository.delete(profession);
    }
}
