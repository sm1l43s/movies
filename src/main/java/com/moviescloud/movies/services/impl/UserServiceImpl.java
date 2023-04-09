package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.User;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repositories.UserRepository;
import com.moviescloud.movies.services.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    final UserRepository userRepository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        log.info("Getting a list of users by page request{}", pageable);
        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(Long id) {
        log.info("Getting user by id{}", id);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id=" + id + " not found"));
    }

    @Override
    public User save(User user) {
        log.info("Save user{}", user);
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        log.info("Delete user{}", user);
        userRepository.delete(user);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        log.info("Check exists if email{}", email);
        return userRepository.existsUserByEmail(email);
    }
}
