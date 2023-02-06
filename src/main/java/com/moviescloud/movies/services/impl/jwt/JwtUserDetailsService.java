package com.moviescloud.movies.services.impl.jwt;

import com.moviescloud.movies.exceptions.UnauthorizedException;
import com.moviescloud.movies.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService {
   final UserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UnauthorizedException("Wrong email address or password"));
    }
}
