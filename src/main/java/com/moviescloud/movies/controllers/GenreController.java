package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Genre;
import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.services.IGenreService;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Genres", description = "Набор методов для работы с данными о жанрах фильмов")
@RestController
@RequestMapping("/api/v1/genres")
public class GenreController {

    IGenreService genreService;

    @Autowired
    public GenreController(IGenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(summary = "Получить список жанров по различным фильтрам",
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
    @GetMapping
    public Response<Genre> getAll(
            @Parameter(description = "Номер страницы")
            @RequestParam(name ="page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество элементов в списке")
            @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
            @Parameter(description = "Сортировка выводимых значений по - \"id\" (идентификатору) или \"name\" (названию жанра)")
            @RequestParam(name = "order", required = false, defaultValue = "id") String order) {
        Page<Genre> pages = genreService.findAll(PageRequest.of(page, pageSize, Sort.by(order)));
        return new Response<>(HttpStatus.OK, pages.getContent(), pages.getTotalElements(), pages.getTotalPages());
    }

    @Operation(summary = "Получить данные о жанре по идентификатору",
            description = "Возвращает базовые данные о жанре.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Genre.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Жанр не найден",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public Genre getById(
            @Parameter(description = "идентификатор жанра")
            @PathVariable Long id) {
        return genreService.findById(id);
    }

    @Operation(summary = "Добавить новый жанр фильма",
            description = "Добовляет и возвращает базовые данные о новом жанре фильма.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Genre.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект нового жанра",
                    content = @Content
            )
    })
    @PostMapping
    public Genre addGenre(@Parameter(description = "JSON структура объекта жанра. Значение \"id\" - не обязательное (\"id\" = \"null\")",
                                     content = @Content(schema = @Schema(implementation = Genre.class)))
                          @RequestBody Genre genre) {
        return genreService.save(genre);
    }

    @Operation(summary = "Изменить данные о жанре фильма",
            description = "Изменяет и возвращает базовые данные о новом жанре фильма.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Genre.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект жанра",
                    content = @Content
            )
    })
    @PutMapping Genre editGenre(@Parameter(description = "JSON структура объекта жанра",
            content = @Content(schema = @Schema(implementation = Genre.class)))
                                    @RequestBody Genre genre) {
        return genreService.save(genre);
    }

    @Operation(summary = "Удалить данные о жанре фильма",
            description = "Удаляет данный жанр фильма.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект жанра",
                    content = @Content
            )
    })
    @DeleteMapping
    public ResponseEntity<?> deleteGenre(@Parameter(description = "JSON структура объекта жанра",
            content = @Content(schema = @Schema(implementation = Genre.class)))
                                             @RequestBody Genre genre) {
        genreService.delete(genre);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удалить данные о жанре фильма по идентификатору",
            description = "Удаляет данный жанр фильма.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@Parameter(description = "идентификатор удаляемого жанра фильма")
                                         @PathVariable Long id) {
        genreService.delete(genreService.findById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
