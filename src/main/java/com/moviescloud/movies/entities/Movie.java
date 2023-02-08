package com.moviescloud.movies.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Size(min = 2, max = 256)
    private String nameRu;
    @NotBlank
    @Size(min = 2, max = 256)
    private String nameEn;
    @NotBlank
    @Size(min = 5, max = 1024)
    private String posterUrl;
    @NotBlank
    @Size(max = 1024)
    private String trailerUrl;
    @Lob
    @NotBlank
    @Size(max = 64000)
    private String description;
    @NotBlank
    @Size(min = 5, max = 256)
    private String slogan;
    @PastOrPresent
    private Date year;
    @NotBlank
    @Size(max = 1000)
    private int movieLength;
    @ManyToMany
    private List<Genre> genres;
    @NotBlank
    @Size(max = 10)
    private double rating;
    @OneToMany(cascade =  CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;
}
