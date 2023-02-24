package com.moviescloud.movies.congfigs;

import com.moviescloud.movies.exceptions.UnauthorizedException;
import com.moviescloud.movies.services.impl.jwt.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {
    final JwtUserDetailsService jwtUserDetailsService;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationManager(JwtUserDetailsService jwtUserDetailsService, PasswordEncoder passwordEncoder) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails user = jwtUserDetailsService.loadUserByEmail(authentication.getName());
        if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new UnauthorizedException("Wrong email address or password");
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
