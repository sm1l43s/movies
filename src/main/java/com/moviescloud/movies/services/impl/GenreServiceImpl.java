package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Genre;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repositories.GenreRepository;
import com.moviescloud.movies.services.IGenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreServiceImpl implements IGenreService {

    final GenreRepository genreRepository;

    @Override
    public Page<Genre> findAll(Pageable pageable) {
        log.info("Getting a list of genres");
        return genreRepository.findAll(pageable);
    }

    @Override
    public Genre findById(Long id) {
        log.info("Getting a genre by id=" + id);
        return genreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre with id=" + id + " not found"));
    }

    @Override
    public Genre save(Genre genre) {
        log.info("Save genre " + genre);
        return genreRepository.save(genre);
    }

    @Override
    public void delete(Genre genre) {
        log.info("Delete genre " + genre);
        genreRepository.delete(genre);
    }
}
