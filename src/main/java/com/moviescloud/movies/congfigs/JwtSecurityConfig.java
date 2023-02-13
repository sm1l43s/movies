package com.moviescloud.movies.congfigs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class JwtSecurityConfig {
    private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**"
    };

    private static final String[] GET_REQUEST_WHITE_LIST = {
            "/",
            "/api/v1/genres",
            "/api/v1/genres/**",
            "/api/v1/staff",
            "/api/v1/staff/**",
            "/api/v1/movies",
            "/api/v1/movies/**",
            "/api/v1/profession",
            "/api/v1/profession/**",
            "/api/v1/users",
            "/api/v1/users/{id}"
    };

    final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public JwtSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.cors().and()
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers(AUTH_WHITE_LIST).permitAll()
                .antMatchers(HttpMethod.GET, GET_REQUEST_WHITE_LIST).permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/**").hasRole("ADMIN")
                .anyRequest().hasRole("USER").and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
