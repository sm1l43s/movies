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

    private final String[] URL_MOVIE_LIST = {
            "/api/v1/movies",
            "/api/v1/movies/{id}"
    };

    private final String[] URL_STAFF_LIST = {
            "/api/v1/staff",
            "/api/v1/staff/**"
    };

    private final String[] URL_USER_LIST = {
            "/api/v1/users",
            "/api/v1/users/{id}"
    };

    private final String[] URL_REVIEW_LIST = {
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
        return http
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers(AUTH_WHITE_LIST).permitAll()
                .antMatchers(HttpMethod.GET).permitAll()

                .antMatchers(HttpMethod.GET, URL_MOVIE_LIST).hasAuthority("GET_MOVIE")
                .antMatchers(HttpMethod.POST, URL_MOVIE_LIST).hasAuthority("CREATE_POST")
                .antMatchers(HttpMethod.PUT, URL_MOVIE_LIST).hasAuthority("EDIT_POST")
                .antMatchers(HttpMethod.DELETE, URL_MOVIE_LIST).hasAuthority("DELETE_POST")

                .antMatchers(HttpMethod.POST, "/api/v1/{id}/votes").hasAuthority("VOTES_MOVIE")

                .antMatchers(HttpMethod.GET, URL_STAFF_LIST).hasAuthority("GET_STAFF")
                .antMatchers(HttpMethod.POST, URL_STAFF_LIST).hasAuthority("CREATE_STAFF")
                .antMatchers(HttpMethod.PUT, URL_STAFF_LIST).hasAuthority("EDIT_STAFF")
                .antMatchers(HttpMethod.DELETE, URL_STAFF_LIST).hasAuthority("DELETE_STAFF")

                .antMatchers(HttpMethod.GET, URL_REVIEW_LIST).hasAuthority("GET_REVIEW")
                .antMatchers(HttpMethod.POST, URL_REVIEW_LIST).hasAuthority("CREATE_REVIEW")
                .antMatchers(HttpMethod.PUT, URL_REVIEW_LIST).hasAuthority("EDIT_REVIEW")
                .antMatchers(HttpMethod.DELETE, URL_REVIEW_LIST).hasAuthority("DELETE_REVIEW")

                .antMatchers(HttpMethod.GET, URL_USER_LIST).hasAuthority("GET_USER")
                .antMatchers(HttpMethod.POST, URL_USER_LIST).hasAuthority("CREATE_USER")
                .antMatchers(HttpMethod.PUT, URL_USER_LIST).hasAuthority("EDIT_USER")
                .antMatchers(HttpMethod.DELETE, URL_USER_LIST).hasAuthority("DELETE_USER")

                .antMatchers(HttpMethod.PUT, "/api/v1/users/{id}/privileges").hasAuthority("EDIT_PRIVILEGE")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
