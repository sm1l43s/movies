package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Movie;
import com.moviescloud.movies.entities.Person;
import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.exceptions.AppException;
import com.moviescloud.movies.services.IMovieService;
import com.moviescloud.movies.services.IPersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Tag(name = "Staff", description = "Набор методов для работы с данными об актерах, режиссерах и т.д.")
@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class PersonController {

    private final IPersonService personService;
    private final IMovieService movieService;

    @Operation(summary = "Получить список актеров, режиссеров и т.д. по различным фильтрам",
            description = "Возвращает список с пагинацией. Каждая страница содержит по умолчанию 10 элементов.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)
                            )
                    }
            )
    })
    @GetMapping
    public Response<Person> getAll(
            @Parameter(description = "Номер страницы")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество элементов в списке")
            @RequestParam(name = "size", required = false, defaultValue = "50") int pageSize,
            @Parameter(description = "Сортировка выводимых значений по полю")
            @RequestParam(name = "order", required = false, defaultValue = "id") String order) {
        Page<Person> pages = personService.findAll(PageRequest.of(page, pageSize, Sort.by(order)));
        return new Response<>(HttpStatus.OK, pages.getContent(), pages.getTotalElements(), pages.getTotalPages());
    }

    @Operation(summary = "Получить данные о конкретной персоне по идентификатору",
            description = "Возвращает базовые данные о персоне.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Person.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Персона не найден",
                    content = @Content(
                            schema = @Schema(implementation = AppException.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public Person getById(
            @Parameter(description = "идентификатор персоны")
            @PathVariable Long id) {
        return personService.findById(id);
    }

    @Operation(summary = "Получить список фильмов для данной персоны",
            description = "Возвращает список жанров с пагинацией. Каждая страница содержит по умолчанию 10 элементов.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Response.class)
                            )
                    }
            )
    })
    @GetMapping("/{id}/movies")
    public Response<Movie> getAllMoviesByPersonId(
            @Parameter(description = "идентификатор персоны")
            @PathVariable long id) {
        List<Movie> movies = personService.findById(id).getMovies();
        return new Response<>(HttpStatus.OK, movies, movies.size(), 0);
    }

    @Operation(summary = "Изменить данные о фильмах для данной персоны",
            description = "Изменяет и возвращает базовые данные о фильмах для данной персоны.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Movie.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект",
                    content = @Content
            )
    })
    @PutMapping("/{id}/movies")
    public Iterable<Movie> addMoviesToPerson(
            @Parameter(description = "идентификатор персоны")
            @PathVariable long id,
            @Parameter(description = "Список изменненных фильмов для данной персоны")
            @RequestBody List<Movie> movies) {
        Person person = personService.findById(id);
        person.setMovies(mapMovie(movies));
        personService.save(person);
        return person.getMovies();
    }

    @Operation(summary = "Удалить данные о фильмах для данной персоны",
            description = "Удаляет данные о фильмах для персоны.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}/movies")
    public ResponseEntity<HttpStatus> clearMoviesList(
            @Parameter(description = "идентификатор персоны")
            @PathVariable long id) {
        Person person = personService.findById(id);
        person.setMovies(Collections.emptyList());
        personService.save(person);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Добавить новую персону (актера, режиссера и т.д.)",
            description = "Добовляет и возвращает базовые данные о новой персоне.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Person.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект нового жанра",
                    content = @Content
            )
    })
    @PostMapping
    public Person add(@RequestBody Person person) {
        return personService.save(person);
    }

    @Operation(summary = "Изменить данные о персоне",
            description = "Изменяет и возвращает базовые данные о персоне.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Person.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект",
                    content = @Content
            )
    })
    @PutMapping
    public Person edit(@RequestBody Person person) {
        person.setMovies(mapMovie(person.getMovies()));
        return personService.save(person);
    }

    @Operation(summary = "Удалить данные о персоне",
            description = "Удаляет данную персону.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект",
                    content = @Content
            )
    })
    @DeleteMapping
    public ResponseEntity<HttpStatus> delete(
            @Parameter(description = "JSON структура объекта персоны", content = @Content(schema = @Schema(implementation = Person.class)))
            @RequestBody Person person) {
        personService.delete(person);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удалить данные о персоне по идентификатору",
            description = "Удаляет данную персону.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(
            @Parameter(description = "идентификатор персоны")
            @PathVariable long id) {
        personService.delete(personService.findById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private List<Movie> mapMovie(List<Movie> movies) {
        List<Movie> movieList = new ArrayList<>();
        for (Movie movie : movies) {
            movieList.add(movieService.findById(movie.getId()));
        }
        return movieList;
    }

}
