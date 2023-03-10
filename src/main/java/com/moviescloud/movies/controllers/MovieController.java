package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Genre;
import com.moviescloud.movies.entities.Movie;
import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.entities.User;
import com.moviescloud.movies.exceptions.AppException;
import com.moviescloud.movies.services.IGenreService;
import com.moviescloud.movies.services.IMovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Operation(summary = "Получить список фильмов по различным фильтрам",
            description = "Возвращает список фильмов с пагинацией. Каждая страница содержит по умолчанию 10 элементов.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Movie.class)
                            )
                    }
            )
    })
    @GetMapping
    public Response<Movie> getMovies(
            @Parameter(description = "Номер страницы")
            @RequestParam(name ="page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество элементов в списке")
            @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
            @Parameter(description = "Сортировка выводимых значений по указанному полю")
            @RequestParam(name = "order", required = false, defaultValue = "id") String order) {
        Page<Movie> pages = movieService.findAll(PageRequest.of(page, pageSize, Sort.by(order)));
        return new Response<>(HttpStatus.OK, pages.getContent(), pages.getTotalElements(), pages.getTotalPages());
    }

    @Operation(summary = "Получить данные о фильме по его идентификатору",
            description = "Возвращает базовые данные о фильме.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Movie.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Фильм не найден",
                    content = @Content (
                            schema = @Schema(implementation = AppException.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public Movie getMovie(
            @Parameter(description = "идентификатор фильма")
            @PathVariable long id) {
        return movieService.findById(id);
    }

    @Operation(summary = "Добавить новый фильм",
            description = "Добовляет и возвращает базовые данные о новом фильме.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Movie.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект",
                    content = @Content
            )
    })
    @PostMapping
    public Movie add(
            @Parameter(description = "JSON структура объекта фильм.",
                    content = @Content(schema = @Schema(implementation = Movie.class)))
            @RequestBody Movie movie) {
        movie.setGenres(mapGenres(movie));
        movie.setNumberOfVotes(0L);
        movie.setVotesScore(0L);
        return movieService.save(movie);
    }

    @Operation(summary = "Изменить данные о фильме",
            description = "Изменяет и возвращает базовые данные о фильме.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Movie.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект",
                    content = @Content
            )
    })
    @PutMapping
    public Movie edit(
            @Parameter(description = "JSON структура объекта фильм",
                    content = @Content(schema = @Schema(implementation = Movie.class)))
            @RequestBody Movie movie) {
        movie.setGenres(mapGenres(movie));
        return movieService.save(movie);
    }

    @Operation(summary = "Удалить данные о фильме",
            description = "Удаляет данный фильм.")
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
    public ResponseEntity<HttpStatus> deleteMovie(
            @Parameter(description = "JSON структура объекта жанра",
                    content = @Content(schema = @Schema(implementation = Movie.class)))
            @RequestBody Movie movie) {
        movieService.delete(movie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удалить данные о фильме по идентификатору",
            description = "Удаляет данный фильм.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMovie(
            @Parameter(description = "идентификатор удаляемого фильма.")
            @PathVariable Long id) {
        movieService.delete(movieService.findById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Метод позволяет поставить оценку фильму",
            description = "В качестве параметра выступает ид фильма и оценка по 10-ти бальной системе.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Неверное значение рейтинга. Рейтинг должен быть между 0 и 10",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Данный пользователь уже оценил данный ресурс.",
                    content = @Content
            )
    })
    @PostMapping("/{id}/votes")
    public ResponseEntity<HttpStatus> vote(@PathVariable Long id,
                                  @RequestParam(name = "score") Integer score) {
        if (score < 0 || score > 10) return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);

        Movie movie = movieService.findById(id);
        movie.setVotesScore(movie.getVotesScore() + score);
        movie.setNumberOfVotes(movie.getNumberOfVotes() + 1);

        List<User> users = movie.getVoteUsers();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (User u: users) {
            if (u.getId() == user.getId()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        users.add(user);
        movie.setVoteUsers(users);

        movieService.save(movie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Метод позволяет получить список пользователей поставивших оценку фильму",
            description = "Возвращает список пользователей оценивших по 10-ти бальной системы фильм")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))
            )
    })
    @GetMapping("/{id}/votes")
    public Iterable<User> getVoteUsersByMovieId(@PathVariable Long id) {
        Movie movie = movieService.findById(id);
        return movie.getVoteUsers();
    }

    private List<Genre> mapGenres(Movie movie) {
        List<Genre> genres = new ArrayList<>();
        for (Genre genre: movie.getGenres()) {
            genres.add(genreService.findById(genre.getId()));
        }
        return genres;
    }
}
