package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.entities.Privilege;
import com.moviescloud.movies.entities.User;
import com.moviescloud.movies.exceptions.AppException;
import com.moviescloud.movies.services.IPrivilegeService;
import com.moviescloud.movies.services.IUserService;
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

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Users", description = "Набор методов для работы с данными о пользователях приложения.")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    final IUserService userService;
    final IPrivilegeService privilegeService;

    @Autowired
    public UserController(IUserService userService, IPrivilegeService privilegeService) {
        this.userService = userService;
        this.privilegeService = privilegeService;
    }

    @Operation(summary = "Получить список пользователй по различным фильтрам.",
            description = "Возвращает список пользователей приложения с пагинацией. Каждая страница содержит по умолчанию 10 элементов.")
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
    public Response<User> getAllUsers(
            @Parameter(description = "Номер страницы")
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество элементов в списке")
            @RequestParam(name = "size", required = false, defaultValue = "50") int pageSize,
            @Parameter(description = "Сортировка выводимых значений по полю")
            @RequestParam(name = "order", required = false, defaultValue = "id") String order) {
        Page<User> pages = userService.findAll(PageRequest.of(page, pageSize, Sort.by(order)));
        return new Response<>(HttpStatus.OK, pages.getContent(), pages.getTotalElements(), pages.getTotalPages());
    }

    @Operation(summary = "Получить данные о пользователе по его идентификатору.",
            description = "Возвращает базовые данные о пользователе.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            schema = @Schema(implementation = AppException.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public User getUserById(
            @Parameter(description = "идентификатор пользователя.")
            @PathVariable Long id) {
        return userService.findById(id);
    }

    @Operation(summary = "Получить данные о правах пользователя (его роли) по идентификатору",
            description = "Возвращает список прав (ролей) данного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = Privilege.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            schema = @Schema(implementation = AppException.class)
                    )
            )
    })
    @GetMapping("/{id}/privileges")
    public Iterable<Privilege> getPrivilegesByUser(
            @Parameter(description = "идентификатор пользователя")
            @PathVariable Long id) {
        return userService.findById(id).getPrivileges();
    }

    @Operation(summary = "Изменить права у пользователя (редактирование ролей) по идентификатору.",
            description = "Возвращает измененные роли пользователя. Данная операция доступна пользователям с правами администратора.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Privilege.class)))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            schema = @Schema(implementation = AppException.class)
                    )
            )
    })
    @PutMapping("/{id}/privileges")
    public Iterable<Privilege> editPrivilegesToUserById(
            @Parameter(description = "идентификатор пользователя")
            @PathVariable Long id,
            @Parameter(description = "Список ролей которыми должен обладать пользователь")
            @RequestBody List<Privilege> privileges) {
        User user = userService.findById(id);
        user.setPrivileges(mapToPrivilegesUser(privileges));
        userService.save(user);
        user = userService.findById(id);
        return user.getPrivileges();
    }

    @Operation(summary = "Редактирование информации о пользователе",
            description = "Позволяет изменить данные о пользователе.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content(schema = @Schema(implementation = User.class))
            )
    })
    @PutMapping
    public User editUserInfo(@RequestBody User user) {
        User userFromDB = userService.findById(user.getId());
        user.setPrivileges(userFromDB.getPrivileges());
        user.setPassword(userFromDB.getPassword());
        user.setEmail(userFromDB.getEmail());
        return userService.save(user);
    }

    @Operation(summary = "Удалить данные о пользователе",
            description = "Удаляет данного пользователя из системы.")
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
    public ResponseEntity<HttpStatus> deleteUser(
            @Parameter(description = "JSON структура объекта пользователя",
                    content = @Content(schema = @Schema(implementation = User.class)))
            @RequestBody User user) {
        userService.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удалить данные о пользователе по идентификатору",
            description = "Удаляет данного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(
            @Parameter(description = "идентификатор удаляемого пользователя")
            @PathVariable Long id) {
        userService.delete(userService.findById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private List<Privilege> mapToPrivilegesUser(List<Privilege> rawPrivileges) {
        List<Privilege> privileges = new ArrayList<>();
        for (Privilege privilege : rawPrivileges) {
            privileges.add(privilegeService.findById(privilege.getId()));
        }
        return privileges;
    }
}
