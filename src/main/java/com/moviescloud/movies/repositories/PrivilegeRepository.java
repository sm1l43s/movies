package com.moviescloud.movies.repositories;

import com.moviescloud.movies.entities.Privilege;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PrivilegeRepository extends PagingAndSortingRepository<Privilege, Long> {
    Optional<Privilege> findByPrivilegeName(String privilegeName);
}
