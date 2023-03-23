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
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 256)
    private String nameRu;

    @NotBlank
    @Size(min = 5, max = 256)
    private String nameEn;

    @NotBlank
    @Size(max = 256)
    private String posterUrl;

    @PastOrPresent
    private Date birthday;

    @NotBlank
    @OneToOne
    private Country birthPlace;

    @ManyToMany
    @JsonIgnore
    private List<Movie> movies;

    @OneToMany
    private List<Profession> professions;
}
