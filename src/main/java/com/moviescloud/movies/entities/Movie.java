package com.moviescloud.movies.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private String nameRu;
    private String nameEn;
    private String posterUrl;
    @Lob
    private String description;
    private String slogan;
    private Date year;
    private int movieLength;
    @ManyToMany
    private List<Genre> genres;
    private double rating;
}
