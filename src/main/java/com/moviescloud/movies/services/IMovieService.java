package com.moviescloud.movies.services;

import com.moviescloud.movies.entities.Movie;
import org.springframework.data.domain.Pageable;

public interface IMovieService {

    Iterable<Movie> findAll(Pageable pageable);
    Movie findById(Long id);
    Movie save(Movie movie);

}
