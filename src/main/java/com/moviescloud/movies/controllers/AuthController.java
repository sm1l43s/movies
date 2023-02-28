package com.moviescloud.movies.controllers;

import com.moviescloud.movies.authentication.AuthenticationRequest;
import com.moviescloud.movies.authentication.AuthenticationResponse;
import com.moviescloud.movies.congfigs.CustomAuthenticationManager;
import com.moviescloud.movies.entities.Privilege;
import com.moviescloud.movies.entities.User;
import com.moviescloud.movies.exceptions.AppException;
import com.moviescloud.movies.exceptions.UnauthorizedException;
import com.moviescloud.movies.services.IPrivilegeService;
import com.moviescloud.movies.services.IUserService;
import com.moviescloud.movies.services.impl.jwt.JwtTokenService;
import com.moviescloud.movies.services.impl.jwt.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Authentication", description = "Набор методов для авторизации и регистрации пользователя.")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    final JwtUserDetailsService jwtUserDetailsService;
    final JwtTokenService jwtTokenService;
    final CustomAuthenticationManager authenticationManager;
    final IUserService userService;
    final PasswordEncoder passwordEncoder;
    final IPrivilegeService privilegeService;

    @Autowired
    public AuthController(JwtUserDetailsService jwtUserDetailsService,
                          JwtTokenService jwtTokenService,
                          CustomAuthenticationManager authenticationManager,
                          IUserService userService,
                          PasswordEncoder passwordEncoder,
                          IPrivilegeService privilegeService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.privilegeService = privilegeService;
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
                    content = @Content(
                            schema = @Schema(implementation = AppException.class)
                    )
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
                    content = @Content(
                            schema = @Schema(implementation = AppException.class)
                    )
            )
    })
    @PostMapping("/signup")
    public HttpStatus signup(
            @Parameter(description = "Емайл адрес и пароль для регистрации.")
            @RequestBody AuthenticationRequest authenticationRequest) {

        if (userService.existsUserByEmail(authenticationRequest.getEmail())) {
            throw new UnauthorizedException("Wrong email address or password!");
        }

        List<Privilege> privileges = new ArrayList<>();
        privileges.add(privilegeService.findByName("GET_REVIEW"));
        privileges.add(privilegeService.findByName("CREATE_REVIEW"));
        privileges.add(privilegeService.findByName("GET_STAFF"));
        privileges.add(privilegeService.findByName("GET_MOVIE"));
        privileges.add(privilegeService.findByName("VOTES_MOVIE"));

        User user = new User(authenticationRequest.getEmail(), passwordEncoder.encode(authenticationRequest.getPassword()),
                null, null, null, privileges);
        userService.save(user);
        return HttpStatus.CREATED;
    }

    @Operation(summary = "Метод для получения информации о пользователе.",
            description = "Позволяет получить данные об авторизованном пользователе.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Данный пользователь не авторизован.",
                    content = @Content(
                            schema = @Schema(implementation = AppException.class)
                    )
            )
    })
    @GetMapping("/me")
    public User getInfo() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            throw new UnauthorizedException("You are not authorized. Please log in!");
        }
    }
}
