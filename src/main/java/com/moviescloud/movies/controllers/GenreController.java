package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Genre;
import com.moviescloud.movies.services.IGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreController {

    IGenreService genreService;

    @Autowired
    public GenreController(IGenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Iterable<Genre> getAll(
            @RequestParam(name ="page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy) {

        return genreService.findAll(PageRequest.of(page, pageSize, Sort.by(sortBy)));
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable Long id) {
        return genreService.findById(id);
    }

    @PostMapping
    public Genre addGenre(@RequestBody Genre genre) {
        return genreService.save(genre);
    }

    @PutMapping Genre editGenre(@RequestBody Genre genre) {
        return genreService.save(genre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id) {
        genreService.delete(genreService.findById(id));
        return new ResponseEntity(HttpStatus.OK);
    }

}
