package com.moviescloud.movies.controllers;

import com.moviescloud.movies.dto.MovieDto;
import com.moviescloud.movies.entities.*;
import com.moviescloud.movies.exceptions.AppException;
import com.moviescloud.movies.services.ICountryService;
import com.moviescloud.movies.services.IGenreService;
import com.moviescloud.movies.services.IMovieService;
import com.moviescloud.movies.services.ITypeServices;
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

import java.util.List;

@Tag(name = "Movies", description = "Набор методов для работы с данными о фильмах.")
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    final IMovieService movieService;

    final IGenreService genreService;

    final ITypeServices typeServices;

    final ICountryService countryService;

    @Autowired
    public MovieController(IMovieService movieService, IGenreService genreService,
                           ITypeServices typeServices, ICountryService countryService) {
        this.movieService = movieService;
        this.genreService = genreService;
        this.typeServices = typeServices;
        this.countryService = countryService;
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
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество элементов в списке")
            @RequestParam(name = "size", required = false, defaultValue = "50") int pageSize,
            @Parameter(description = "Сортировка выводимых значений по указанному полю")
            @RequestParam(name = "order", required = false, defaultValue = "id") String order,
            @Parameter(description = "Поиск по ключевому слову, которое встречается в названии фильма (сериала, тв-шоу)")
            @RequestParam(name = "keyword", required = false) String keyword,
            @Parameter(description = "Сортировка по странам (id страны). Например countries=1")
            @RequestParam(name = "countries", required = false) Long idCountries,
            @Parameter(description = "Сортировка по жанрам (id жанра). Например genres=1")
            @RequestParam(name = "genres", required = false) Long idGenres,
            @Parameter(description = "Сортировка по типам - фильм, сериал, тв-шоу, мини-сериал (id типа). Например type=1")
            @RequestParam(name = "type", required = false) Long idType) {

        Page<Movie> pages = null;
        if (keyword != null) {
            pages = movieService.findAllByName(PageRequest.of(page, pageSize, Sort.by(order)), keyword);
        }

        if (idCountries != null) {
            Country country = countryService.findById(idCountries);
            pages = movieService.findAllByCountries(PageRequest.of(page, pageSize, Sort.by(order)), country);
        }

        if (idGenres != null) {
            Genre genre = genreService.findById(idGenres);
            pages = movieService.findAllByGenres(PageRequest.of(page, pageSize, Sort.by(order)), genre);
        }

        if (idType != null) {
            Type type = typeServices.findById(idType);
            pages = movieService.findAllByType(PageRequest.of(page, pageSize, Sort.by(order)), type);
        }

        if (pages == null) {
            pages = movieService.findAll(PageRequest.of(page, pageSize, Sort.by(order)));
        }
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
                    content = @Content(
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
                    content = @Content(schema = @Schema(implementation = MovieDto.class)))
            @RequestBody MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setNameRu(movieDto.getNameRu());
        movie.setNameEn(movieDto.getNameEn());
        movie.setPosterUrl(movieDto.getPosterUrl());
        movie.setTrailerUrl(movieDto.getTrailerUrl());
        movie.setDescription(movieDto.getDescription());
        movie.setSlogan(movieDto.getSlogan());
        movie.setYear(movieDto.getYear());
        movie.setMovieLength(movieDto.getMovieLength());
        movie.setGenres(movieDto.getGenres());
        movie.setCountries(movieDto.getCountries());
        movie.setNumberOfVotes(0L);
        movie.setVotesScore(0L);
        movie.setType(movieDto.getType());
        movie.setRatingImdb(movieDto.getRatingImdb());
        movie.setRatingKinopoisk(movieDto.getRatingKinopoisk());
        return movieService.save(movie);
    }

    @PostMapping("/list")
    public ResponseEntity addList(@RequestBody List<MovieDto> movieDtoList) {
        for (MovieDto m: movieDtoList) {
            add(m);
        }
        return new ResponseEntity(HttpStatus.OK);
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
        movie.setGenres(movie.getGenres());
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
        for (User u : users) {
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
}
