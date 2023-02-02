package com.moviescloud.movies.repositories;

import com.moviescloud.movies.entities.Profession;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionRepository extends PagingAndSortingRepository<Profession, Long> {
}
