package com.moviescloud.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDto {
    @Email
    private String email;
    @NotBlank
    @Size(max = 255)
    private String password;
}
