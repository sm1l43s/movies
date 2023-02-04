package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Profession;
import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.services.IProfessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Profession", description = "Набор методов для работы с данными о профессиях персонала фильмов.")
@RestController
@RequestMapping("/api/v1/profession")
public class ProfessionController {

    IProfessionService professionService;

    @Autowired
    public ProfessionController(IProfessionService professionService) {
        this.professionService = professionService;
    }

    @Operation(summary = "Получить список профессий по различным фильтрам",
            description = "Возвращает список профессий с пагинацией. Каждая страница содержит по умолчанию 10 элементов.")
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
    public Response<Profession> getAll(
            @Parameter(description = "Номер страницы")
            @RequestParam(name ="page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество элементов в списке")
            @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
            @Parameter(description = "Сортировка выводимых значений по - \"id\" (идентификатору) или \"name\" (названию жанра)")
            @RequestParam(name = "order", required = false, defaultValue = "id") String order) {
        Page<Profession> pages = professionService.findAll(PageRequest.of(page, pageSize, Sort.by(order)));
        return new Response<>(HttpStatus.OK, pages.getContent(), pages.getTotalElements(), pages.getTotalPages());
    }

    @Operation(summary = "Получить данные о профессии по идентификатору",
            description = "Возвращает базовые данные о профессии.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Profession.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Жанр не найден",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    Profession getById(
            @Parameter(description = "идентификатор профессии.")
            @PathVariable long id) {
        return professionService.findById(id);
    }

    @Operation(summary = "Добавить новый вид профессии фильма",
            description = "Добовляет и возвращает базовые данные о новом фиде профессии фильма.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Profession.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект",
                    content = @Content
            )
    })
    @PostMapping
    Profession add(
            @Parameter(description = "JSON структура объекта префесии. Значение \"id\" - не обязательное (\"id\" = \"null\")",
                    content = @Content(schema = @Schema(implementation = Profession.class)))
            @RequestBody Profession profession) {
        return professionService.save(profession);
    }

    @Operation(summary = "Изменить данные о профессии фильма",
            description = "Изменяет и возвращает базовые данные о профессии фильма.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Profession.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект жанра",
                    content = @Content
            )
    })
    @PutMapping
    Profession edit(
            @Parameter(description = "JSON структура объекта префесии.", content = @Content(schema = @Schema(implementation = Profession.class)))
            @RequestBody Profession profession) {
        return professionService.save(profession);
    }

    @Operation(summary = "Удалить данные о профессии фильма",
            description = "Удаляет данную профессию фильма.")
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
    ResponseEntity<?> delete(
            @Parameter(description = "JSON структура объекта префесии.", content = @Content(schema = @Schema(implementation = Profession.class)))
            @RequestBody Profession profession) {
        professionService.delete(profession);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удалить данные о профессии по идентификатору",
            description = "Удаляет данный вид профессии.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(
            @Parameter(description = "идентификатор удаляемой профессии")
            @PathVariable long id) {
        professionService.delete(professionService.findById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
