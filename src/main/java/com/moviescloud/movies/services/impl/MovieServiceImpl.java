package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Movie;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repositories.MovieRepository;
import com.moviescloud.movies.services.IMovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MovieServiceImpl implements IMovieService {

    MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Page<Movie> findAll(Pageable pageable) {
        log.info("Getting a list of movies");
        return movieRepository.findAll(pageable);
    }

    @Override
    public Movie findById(Long id) {
        log.info("Getting a movie by id=" + id);
        return movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie with id=" + id + " not found"));
    }

    @Override
    public Movie save(Movie movie) {
        log.info("Save movie:" + movie);
        return movieRepository.save(movie);
    }

    @Override
    public void delete(Movie movie) {
        log.info("Delete movie: " + movie);
        movieRepository.delete(movie);
    }
}
