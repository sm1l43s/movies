package com.moviescloud.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.moviescloud.common", "com.moviescloud.movies"})
@EntityScan({"com.moviescloud.movies.entities", "com.moviescloud.common.entities"})
public class MoviesApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoviesApplication.class, args);
    }
}
