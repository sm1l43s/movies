package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Movie;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repoitories.MovieRepository;
import com.moviescloud.movies.services.IMovieService;
import lombok.extern.slf4j.Slf4j;
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
    public Iterable<Movie> findAll(Pageable pageable) {
        log.info("Getting a list of movies");
        return movieRepository.findAll(pageable).getContent();
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie with id=" + id + " not found"));
    }

    @Override
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }
}
