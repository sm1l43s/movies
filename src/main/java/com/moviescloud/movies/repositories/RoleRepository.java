package com.moviescloud.movies.repositories;

import com.moviescloud.movies.entities.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Optional;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
