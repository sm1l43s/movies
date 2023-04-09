package com.moviescloud.movies.controllers;

import com.moviescloud.movies.congfigs.CustomAuthenticationManager;
import com.moviescloud.movies.dto.AuthenticationRequestDto;
import com.moviescloud.movies.dto.authentication.AuthenticationRequest;
import com.moviescloud.movies.dto.authentication.AuthenticationResponse;
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
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthController {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenService jwtTokenService;
    private final CustomAuthenticationManager authenticationManager;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final IPrivilegeService privilegeService;

    @Operation(summary = "Метод для авторизации.", description = "Авторизовывает пользователя по емайл адресу" +
            " и паролю и возвращает токен для доступа к ресурсам сервера. Токен действителен 12 часов, после чего требуется переавторизоваться.")
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
            @RequestBody AuthenticationRequestDto authenticationRequest) {
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
            @RequestBody AuthenticationRequest authentication) {

        if (userService.existsUserByEmail(authentication.getEmail())) {
            throw new UnauthorizedException("Wrong email address or password!");
        }

        List<Privilege> privileges = new ArrayList<>();
        privileges.add(privilegeService.findByName("GET_REVIEW"));
        privileges.add(privilegeService.findByName("CREATE_REVIEW"));
        privileges.add(privilegeService.findByName("GET_STAFF"));
        privileges.add(privilegeService.findByName("GET_MOVIE"));
        privileges.add(privilegeService.findByName("VOTES_MOVIE"));

        userService.save(new User(authentication.getEmail(), passwordEncoder.encode(authentication.getPassword()),
                authentication.getFirstName(), authentication.getLastName(), authentication.getBirthDay(), privileges));
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
