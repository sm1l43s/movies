package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Genre;
import com.moviescloud.movies.entities.Movie;
import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.services.IGenreService;
import com.moviescloud.movies.services.IMovieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Movies", description = "Набор методов для работы с данными о фильмах.")
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    final IMovieService movieService;
    final IGenreService genreService;
    @Autowired
    public MovieController(IMovieService movieService, IGenreService genreService) {
        this.movieService = movieService;
        this.genreService = genreService;
    }

    @GetMapping
    public Response<Movie> getMovies(
           @RequestParam(name ="page", required = false, defaultValue = "0")  int page,
           @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
           @RequestParam(name = "order", required = false, defaultValue = "id") String order) {
        Page<Movie> pages = movieService.findAll(PageRequest.of(page, pageSize, Sort.by(order)));
        return new Response<>(HttpStatus.OK, pages.getContent(), pages.getTotalElements(), pages.getTotalPages());
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable long id) {
        return movieService.findById(id);
    }

    @PostMapping
    public Movie add(@RequestBody Movie movie) {
        movie.setGenres(mapGenres(movie));
        return movieService.save(movie);
    }

    @PutMapping
    public Movie edit(@RequestBody Movie movie) {
        movie.setGenres(mapGenres(movie));
        return movieService.save(movie);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMovie(@RequestBody Movie movie) {
        movieService.delete(movie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        movieService.delete(movieService.findById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private List<Genre> mapGenres(Movie movie) {
        List<Genre> genres = new ArrayList<>();
        for (Genre genre: movie.getGenres()) {
            genres.add(genreService.findById(genre.getId()));
        }
        return genres;
    }
}
