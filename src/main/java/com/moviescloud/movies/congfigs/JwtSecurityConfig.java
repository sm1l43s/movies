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

    private static final String[] URL_MOVIE_LIST = {
            "/api/v1/movies",
            "/api/v1/movies/{id}"
    };

    private static final String[] URL_STAFF_LIST = {
            "/api/v1/staff",
            "/api/v1/staff/**"
    };

    private static final String[] URL_USER_LIST = {
            "/api/v1/users",
            "/api/v1/users/{id}"
    };

    private static final String[] URL_REVIEW_LIST = {
            "/api/v1/movies/reviews",
            "/api/v1/movies/{id}/reviews"
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
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(AUTH_WHITE_LIST).permitAll()
                        .requestMatchers(HttpMethod.GET).permitAll()

                        .requestMatchers(HttpMethod.GET, URL_MOVIE_LIST).hasAuthority("GET_MOVIE")
                        .requestMatchers(HttpMethod.POST, URL_MOVIE_LIST).hasAuthority("CREATE_MOVIE")
                        .requestMatchers(HttpMethod.PUT, URL_MOVIE_LIST).hasAuthority("EDIT_MOVIE")
                        .requestMatchers(HttpMethod.DELETE, URL_MOVIE_LIST).hasAuthority("DELETE_MOVIE")

                        .requestMatchers(HttpMethod.POST, "/api/v1/{id}/votes").hasAuthority("VOTES_MOVIE")

                        .requestMatchers(HttpMethod.GET, URL_STAFF_LIST).hasAuthority("GET_STAFF")
                        .requestMatchers(HttpMethod.POST, URL_STAFF_LIST).hasAuthority("CREATE_STAFF")
                        .requestMatchers(HttpMethod.PUT, URL_STAFF_LIST).hasAuthority("EDIT_STAFF")
                        .requestMatchers(HttpMethod.DELETE, URL_STAFF_LIST).hasAuthority("DELETE_STAFF")

                        .requestMatchers(HttpMethod.GET, URL_REVIEW_LIST).hasAuthority("GET_REVIEW")
                        .requestMatchers(HttpMethod.POST, URL_REVIEW_LIST).hasAuthority("CREATE_REVIEW")
                        .requestMatchers(HttpMethod.PUT, URL_REVIEW_LIST).hasAuthority("EDIT_REVIEW")
                        .requestMatchers(HttpMethod.DELETE, URL_REVIEW_LIST).hasAuthority("DELETE_REVIEW")

                        .requestMatchers(HttpMethod.GET, URL_USER_LIST).hasAuthority("GET_USER")
                        .requestMatchers(HttpMethod.POST, URL_USER_LIST).hasAuthority("CREATE_USER")
                        .requestMatchers(HttpMethod.PUT, URL_USER_LIST).hasAuthority("EDIT_USER")
                        .requestMatchers(HttpMethod.DELETE, URL_USER_LIST).hasAuthority("DELETE_USER")

                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/{id}/privileges").hasAuthority("EDIT_PRIVILEGE")
                )
                .sessionManagement(session -> session // Настройка управления сессиями
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
