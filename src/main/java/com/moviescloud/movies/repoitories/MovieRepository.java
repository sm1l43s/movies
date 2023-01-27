package com.moviescloud.movies.repoitories;

import com.moviescloud.movies.entities.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long> {

}
