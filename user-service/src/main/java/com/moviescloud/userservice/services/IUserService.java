package com.moviescloud.userservice.services;

import com.moviescloud.common.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    Page<User> findAll(Pageable pageable);

    User findById(Long id);

    User save(User user);

    void delete(User user);

    boolean existsUserByEmail(String email);
}
