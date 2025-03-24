package com.moviescloud.movies.repositories;

import com.moviescloud.common.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Long> {
}
