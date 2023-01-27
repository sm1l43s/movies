package com.moviescloud.movies.services;
import com.moviescloud.movies.entities.Genre;
import org.springframework.data.domain.Pageable;


public interface IGenreService {
    Iterable<Genre> findAll(Pageable pageable);
    Genre findById(Long id);
    Genre save(Genre genre);
    void delete(Genre genre);
}
