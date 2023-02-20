package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Genre;
import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.services.IGenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class GenreControllerTest {

    private final String URL = "http://localhost:8080/api/v1/genres";
    private final IGenreService genreService;

    @Autowired
    public GenreControllerTest(IGenreService genreService) {
        this.genreService = genreService;
    }

    @Test
    public void whenCreateGenre_thanStatus200() {
        Genre genre = new Genre(null, "Test Genre");
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Genre> response = restTemplate.postForEntity(URL, genre, Genre.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).getId(), notNullValue());
        assertThat(response.getBody().getName(), is("Test Genre"));
        genreService.delete(genreService.findById(response.getBody().getId()));
    }

    @Test
    public void createGenre_whenGetGenre_thanStatus200() {
        Genre genre = new Genre(null, "Test Genre");
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Genre> response = restTemplate.postForEntity(URL, genre, Genre.class);
        Genre createdGenre = restTemplate.getForObject(URL + "/{id}", Genre.class, Objects.requireNonNull(response.getBody()).getId());
        assertThat(createdGenre.getName(), is("Test Genre"));
        genreService.delete(genreService.findById(response.getBody().getId()));
    }

    @Test
    public void whenUpdateGenre_thanStatus200() {
        Genre genre = new Genre(null, "Test Genre");
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Genre> response = restTemplate.postForEntity(URL, genre, Genre.class);
        Objects.requireNonNull(response.getBody()).setName("Update Test Genre");
        ResponseEntity<Genre> responseAfterUpdate = restTemplate.exchange(URL, HttpMethod.PUT, response, Genre.class);
        assertThat(responseAfterUpdate.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(responseAfterUpdate.getBody()).getId(), notNullValue());
        assertThat(responseAfterUpdate.getBody().getName(), is("Update Test Genre"));
        genreService.delete(genreService.findById(responseAfterUpdate.getBody().getId()));
    }

    @Test
    public void createGenre_whenDeleteGenre_thanStatus200() {
        Genre genre = new Genre(null, "Test Genre");
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Genre> response = restTemplate.postForEntity(URL, genre, Genre.class);
        ResponseEntity<Genre> responseAfterDelete = restTemplate.exchange(URL, HttpMethod.DELETE, response, Genre.class);
        assertThat(responseAfterDelete.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void givenGenres_whenGetGenres_thanStatus200() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Response<Genre>> response = restTemplate.exchange(URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }
}
