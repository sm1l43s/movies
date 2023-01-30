package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Movie;
import com.moviescloud.movies.entities.Person;
import com.moviescloud.movies.services.IMovieService;
import com.moviescloud.movies.services.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
public class PersonController {

    IPersonService personService;
    IMovieService movieService;

    @Autowired
    public PersonController(IPersonService personService, IMovieService movieService) {
        this.personService = personService;
        this.movieService = movieService;
    }

    @GetMapping
    public Iterable<Person> getAll(@RequestParam(name ="page", required = false, defaultValue = "0")  int page,
                                   @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(name = "order", required = false, defaultValue = "id") String order) {

        return personService.findAll(PageRequest.of(page, pageSize, Sort.by(order)));
    }

    @GetMapping("/{id}")
    public Person getById(@PathVariable long id) {
        return personService.findById(id);
    }

    @GetMapping("/{id}/movies")
    public Iterable<Movie> getAllMoviesByPerson(@PathVariable long id) {
        return personService.findById(id).getMovies();
    }

    @PutMapping("/{id}/movies")
    public Iterable<Movie> addMoviesToPerson(@PathVariable long id,
                                             @RequestBody List<Movie> movies) {
        Person person = personService.findById(id);
        person.setMovies(mapMovie(movies));
        personService.save(person);
        return person.getMovies();
    }

    @PostMapping
    public Person add(@RequestBody Person person) {
        person.setMovies(mapMovie(person.getMovies()));
        return personService.save(person);
    }

    @PutMapping
    public Person edit(@RequestBody Person person) {
        person.setMovies(mapMovie(person.getMovies()));
        return personService.save(person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        personService.delete(personService.findById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private List<Movie> mapMovie(List<Movie> movies) {
        List<Movie> movieList = new ArrayList<>();
        for (Movie movie: movies) {
            movieList.add(movieService.findById(movie.getId()));
        }
        return movieList;
    }

}
