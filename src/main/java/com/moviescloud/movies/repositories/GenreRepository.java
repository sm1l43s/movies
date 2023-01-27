package com.moviescloud.movies.repositories;

import com.moviescloud.movies.entities.Genre;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends PagingAndSortingRepository<Genre, Long> {
}
