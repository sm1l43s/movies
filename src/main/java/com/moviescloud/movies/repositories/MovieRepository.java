package com.moviescloud.movies.repositories;

import com.moviescloud.movies.entities.Country;
import com.moviescloud.movies.entities.Genre;
import com.moviescloud.movies.entities.Movie;
import com.moviescloud.movies.entities.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long> {
    Page<Movie> findAllByNameRuContainingIgnoreCase(Pageable pageable, String keyword);
    Page<Movie> findAllByType(Pageable pageable, Type type);
    Page<Movie> findAllByGenres(Pageable pageable, Genre genre);
    Page<Movie> findAllByCountries(Pageable pageable, Country country);
}
