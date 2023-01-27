package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Genre;
import com.moviescloud.movies.entities.Movie;
import com.moviescloud.movies.repositories.GenreRepository;
import com.moviescloud.movies.services.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    IMovieService movieService;
    GenreRepository genreRepository;

    @Autowired
    public MovieController(IMovieService movieService, GenreRepository genreRepository) {
        this.movieService = movieService;
        this.genreRepository = genreRepository;
    }

    @GetMapping
    public Iterable<Movie> getMovies(
           @RequestParam(name ="page", required = false, defaultValue = "0") int page,
           @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
           @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy) {

        return movieService.findAll(PageRequest.of(page, pageSize, Sort.by(sortBy)));
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable long id) {
        return movieService.findById(id);
    }

    @PostMapping
    public Movie add(@RequestBody Movie movie) {
        Genre genre = genreRepository.findById(1L).get();
        movie.setGenres(List.of(genre));
        return movieService.save(movie);
    }
}
