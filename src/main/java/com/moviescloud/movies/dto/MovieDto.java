package com.moviescloud.movies.dto;

import com.moviescloud.movies.entities.Country;
import com.moviescloud.movies.entities.Genre;
import com.moviescloud.movies.entities.Type;
import lombok.Value;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.List;

@Value
public class MovieDto {
    @NotBlank
    @Size(min = 2, max = 256)
    String nameRu;

    @NotBlank
    @Size(min = 2, max = 256)
    String nameEn;

    @NotBlank
    @Size(min = 5, max = 1024)
    String posterUrl;

    @NotBlank
    @Size(max = 1024)
    String trailerUrl;

    @NotBlank
    @Size(min = 5, max = 256)
    String slogan;

    @Lob
    @NotBlank
    @Size(max = 64000)
    String description;

    @NotBlank
    @Size(max = 10)
    double ratingImdb;

    @NotBlank
    @Size(max = 10)
    double ratingKinopoisk;

    @PastOrPresent
    String year;

    @NotBlank
    @Size(max = 1000)
    int movieLength;

    Type type;

    List<Genre> genres;

    List<Country> countries;
}
