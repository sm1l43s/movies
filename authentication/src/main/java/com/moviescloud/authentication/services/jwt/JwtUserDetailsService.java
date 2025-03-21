package com.moviescloud.authentication.services.jwt;

import com.moviescloud.common.resttemplates.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService {
    final UserClient userClient;

    public UserDetails loadUserByEmail(String email) {
        return userClient.findByEmail(email);
    }
}
