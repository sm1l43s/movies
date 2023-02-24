package com.moviescloud.movies.repositories;

import com.moviescloud.movies.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Boolean existsUserByEmail(String email);
}
