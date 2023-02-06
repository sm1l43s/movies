package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.entities.Role;
import com.moviescloud.movies.entities.User;
import com.moviescloud.movies.services.IRoleService;
import com.moviescloud.movies.services.IUserService;
import io.swagger.v3.oas.annotations.Parameter;
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
    final IRoleService roleService;
    @Autowired
    public UserController(IUserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public Response<User> getAllUsers(
            @Parameter(description = "Номер страницы")
            @RequestParam(name ="page", required = false, defaultValue = "0") int page,
            @Parameter(description = "Количество элементов в списке")
            @RequestParam(name = "size", required = false, defaultValue = "10") int pageSize,
            @Parameter(description = "Сортировка выводимых значений по полю")
            @RequestParam(name = "order", required = false, defaultValue = "id") String order) {
        Page<User> pages = userService.findAll(PageRequest.of(page, pageSize, Sort.by(order)));
        return new Response<>(HttpStatus.OK, pages.getContent(), pages.getTotalElements(), pages.getTotalPages());
    }

    @GetMapping("/{id}")
    public User getUserById(
            @Parameter(description = "идентификатор пользователя")
            @PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/{id}/roles")
    public Iterable<Role> getRolesByUser(
            @Parameter(description = "идентификатор пользователя")
            @PathVariable Long id) {
        return userService.findById(id).getRoles();
    }

    @PutMapping("/{id}/roles")
    public Iterable<Role> editRolesToUserById(
            @Parameter(description = "идентификатор пользователя")
            @PathVariable Long id,
            @Parameter(description = "Список ролей которыми должен обладать пользователь")
            @RequestBody List<Role> roles) {
        User user = userService.findById(id);
        user.setRoles(roles);
        userService.save(user);
        user = userService.findById(id);
        return user.getRoles();
    }

    @PutMapping
    public User editUserInfo(@RequestBody User user) {
        User userFromDB = userService.findById(user.getId());
        user.setRoles(mapToRoleUser(user.getRoles()));
        user.setPassword(userFromDB.getPassword());
        user.setEmail(userFromDB.getEmail());
        return userService.save(user);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody User user) {
        userService.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.delete(userService.findById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private List<Role> mapToRoleUser(List<Role> rawRoles) {
        List<Role> roles = new ArrayList<>();
        for (Role role: rawRoles) {
            roles.add(roleService.findById(role.getId()));
        }
        return roles;
    }

}
