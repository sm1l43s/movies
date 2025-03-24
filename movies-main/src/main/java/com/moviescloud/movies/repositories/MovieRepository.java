package com.moviescloud.movies.repositories;

import com.moviescloud.common.entities.Country;
import com.moviescloud.common.entities.Genre;
import com.moviescloud.common.entities.Movie;
import com.moviescloud.common.entities.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findAllByNameRuContainingIgnoreCase(Pageable pageable, String keyword);
    Page<Movie> findAllByType(Pageable pageable, Type type);
    Page<Movie> findAllByGenres(Pageable pageable, Genre genre);
    Page<Movie> findAllByCountries(Pageable pageable, Country country);
}
