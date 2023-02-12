package com.moviescloud.movies.controllers;

import com.moviescloud.movies.congfigs.CustomAuthenticationManager;
import com.moviescloud.movies.entities.User;
import com.moviescloud.movies.exceptions.UnauthorizedException;
import com.moviescloud.movies.services.IRoleService;
import com.moviescloud.movies.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.moviescloud.movies.authentication.AuthenticationRequest;
import com.moviescloud.movies.authentication.AuthenticationResponse;
import com.moviescloud.movies.services.impl.jwt.JwtTokenService;
import com.moviescloud.movies.services.impl.jwt.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Tag(name = "Authentication", description = "Набор методов для авторизации и регистрации пользователя.")
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    final JwtUserDetailsService jwtUserDetailsService;
    final JwtTokenService jwtTokenService;
    final CustomAuthenticationManager authenticationManager;
    final IUserService userService;
    final PasswordEncoder passwordEncoder;
    final IRoleService roleService;
    @Autowired
    public AuthController(JwtUserDetailsService jwtUserDetailsService, JwtTokenService jwtTokenService,
                          CustomAuthenticationManager authenticationManager, IUserService userService,
                          PasswordEncoder passwordEncoder, IRoleService roleService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Operation(summary = "Метод для авторизации.", description = "Авторизовывает пользователя по емайл адресу" +
            " и паролю и возрвращает токен для доступа к ресурсам сервера. Токен действителен 12 часов, после чего требуется переавторизоваться.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Неверный емайл адрес или пароль.",
                    content = @Content
            )
    })
    @PostMapping("/signin")
    public AuthenticationResponse signing(
            @Parameter(description = "Емайл адрес и пароль для авторизации")
            @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = jwtUserDetailsService.loadUserByEmail(authenticationRequest.getEmail());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtTokenService.generateToken(userDetails));
        return authenticationResponse;
    }

    @Operation(summary = "Метод для регистрации пользователя",
            description = "Регистрирует пользователя на ресурсе по емайл адресу (используется как логин) и паролю")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Запрос выполнен успешно. Учетная запись создана",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не уникальный емайл адрес.",
                    content = @Content
            )
    })
    @PostMapping("/signup")
    public HttpStatus signup(
            @Parameter(description = "Емайл адрес и пароль для регистрации")
            @RequestBody AuthenticationRequest authenticationRequest) {

        if (userService.existsUserByEmail(authenticationRequest.getEmail())) {
            throw new UnauthorizedException("Wrong email address or password");
        }

        User user = new User(authenticationRequest.getEmail(),passwordEncoder.encode(authenticationRequest.getPassword()),
                null, null, null, List.of(roleService.findByName("ROLE_USER")));
        userService.save(user);
        return HttpStatus.CREATED;
    }

    @GetMapping("/me")
    public User getInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            return user;
        }
        throw new UnauthorizedException("Error");
    }
}
