package com.moviescloud.movies.services;

import com.moviescloud.common.entities.Country;
import com.moviescloud.common.entities.Genre;
import com.moviescloud.common.entities.Movie;
import com.moviescloud.common.entities.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMovieService {
    Page<Movie> findAll(Pageable pageable);

    Page<Movie> findAllByName(Pageable pageable, String keyword);

    Page<Movie> findAllByType(Pageable pageable, Type type);

    Page<Movie> findAllByGenres(Pageable pageable, Genre genre);

    Page<Movie> findAllByCountries(Pageable pageable, Country country);

    Movie findById(Long id);

    Movie save(Movie movie);

    void delete(Movie movie);
}
