package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Country;
import com.moviescloud.movies.entities.Genre;
import com.moviescloud.movies.entities.Movie;
import com.moviescloud.movies.entities.Type;
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

    final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Page<Movie> findAll(Pageable pageable) {
        log.info("Getting a list of movies");
        return movieRepository.findAll(pageable);
    }

    @Override
    public Page<Movie> findAllByName(Pageable pageable, String keyword) {
        log.info("Getting a list of movies by keyword{}", keyword);
        return movieRepository.findAllByNameRuContainingIgnoreCase(pageable, keyword);
    }

    @Override
    public Page<Movie> findAllByType(Pageable pageable, Type type) {
        log.info("Getting a list of movies by type{}", type);
        return movieRepository.findAllByType(pageable, type);
    }

    @Override
    public Page<Movie> findAllByGenres(Pageable pageable, Genre genre) {
        log.info("Getting a list of movies by genres{}", genre);
        return movieRepository.findAllByGenres(pageable, genre);
    }

    @Override
    public Page<Movie> findAllByCountries(Pageable pageable, Country country) {
        log.info("Getting a list of movie by countries{}", country);
        return movieRepository.findAllByCountries(pageable, country);
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
