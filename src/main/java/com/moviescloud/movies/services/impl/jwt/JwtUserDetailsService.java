package com.moviescloud.movies.services.impl.jwt;

import com.moviescloud.movies.exceptions.UnauthorizedException;
import com.moviescloud.movies.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService {
    final UserRepository userRepository;

    public UserDetails loadUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UnauthorizedException("Wrong email address or password!"));
    }
}
