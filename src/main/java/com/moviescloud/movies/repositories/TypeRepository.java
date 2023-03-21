package com.moviescloud.movies.repositories;

import com.moviescloud.movies.entities.Type;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TypeRepository extends PagingAndSortingRepository<Type, Long> {
}
