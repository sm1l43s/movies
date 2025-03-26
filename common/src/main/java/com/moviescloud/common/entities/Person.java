package com.moviescloud.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import lombok.Setter;

@Entity
@Getter
@Setter
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
