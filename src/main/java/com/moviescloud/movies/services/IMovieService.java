package com.moviescloud.movies.services;

import com.moviescloud.movies.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMovieService {

    Page<Movie> findAll(Pageable pageable);
    Movie findById(Long id);
    Movie save(Movie movie);
    void delete(Movie movie);

}
