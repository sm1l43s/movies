package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.entities.Role;
import com.moviescloud.movies.services.IRoleService;
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

@Tag(name = "Roles", description = "Набор методов для работы с ролями(пользователь, админ и тд) пользователей")
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    final IRoleService roleService;

    @Autowired
    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Получить список ролей пользователй по различным фильтрам",
            description = "Возвращает список ролей с пагинацией. Каждая страница содержит по умолчанию 10 элементов.")
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
    public Response<Role> getAllRoles(
            @Parameter(description = "Номер страницы")
            @RequestParam(name ="page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество элементов в списке")
            @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
            @Parameter(description = "Сортировка выводимых значений по - \"id\" (идентификатору) или \"name\" (названию жанра)")
            @RequestParam(name = "order", required = false, defaultValue = "id") String order) {
        Page<Role> pages = roleService.findAll(PageRequest.of(page, pageSize, Sort.by(order)));
        return new Response<>(HttpStatus.OK, pages.getContent(), pages.getTotalElements(), pages.getTotalPages());
    }

    @Operation(summary = "Получить данные о роли пользователя по идентификатору",
            description = "Возвращает базовые данные о роли пользователя.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Role.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Жанр не найден",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public Role getRoleById(
            @Parameter(description = "идентификатор роли")
            @PathVariable Long id) {
        return roleService.findById(id);
    }

    @Operation(summary = "Добавить новый вид роли пользователя. " +
            "Наименование роли должно быть следующим -> \"ROLE_ИМЯ_РОЛИ\" (ROLE_USER или ROLE_ADMIN и т.п.)",
            description = "Добовляет и возвращает базовые данные о новом виде роли.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Role.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект",
                    content = @Content
            )
    })
    @PostMapping
    public Role addRole(@Parameter(description = "JSON структура объекта роли. Значение \"id\" - не обязательное (\"id\" = \"null\")",
            content = @Content(schema = @Schema(implementation = Role.class)))
                          @RequestBody Role role) {
        return roleService.save(role);
    }

    @Operation(summary = "Изменить данные о роли пользователя. " +
            "Наименование роли должно быть следующим -> \"ROLE_ИМЯ_РОЛИ\" (ROLE_USER или ROLE_ADMIN и т.п.)",
            description = "Изменяет и возвращает базовые данные о роли пользователя." +
                    "Наименование роли должно быть следующим -> \"ROLE_ИМЯ_РОЛИ\" (ROLE_USER или ROLE_ADMIN и т.п.)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно.",
                    content = @Content(schema = @Schema(implementation = Role.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пустой или неправильный JSON объект.",
                    content = @Content
            )
    })
    @PutMapping Role editRole(@Parameter(description = "JSON структура объекта роли",
            content = @Content(schema = @Schema(implementation = Role.class)))
                                @RequestBody Role role) {
        return roleService.save(role);
    }

    @Operation(summary = "Удалить данные о роли пользователя",
            description = "Удаляет данный вид роли пользователя.")
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
    public ResponseEntity<?> deleteRole(
            @Parameter(description = "JSON структура объекта роли", content = @Content(schema = @Schema(implementation = Role.class)))
            @RequestBody Role role) {
        roleService.delete(role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удалить данные о роли пользователя по идентификатору",
            description = "Удаляет данный вид роли пользователя.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(
            @Parameter(description = "идентификатор удаляемого вида роли")
            @PathVariable Long id) {
        roleService.delete(roleService.findById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
