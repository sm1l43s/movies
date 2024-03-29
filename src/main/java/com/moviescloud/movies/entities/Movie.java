package com.moviescloud.movies.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(columnDefinition="character varying(2048)")
    private String nameRu;

    @NotBlank
    @Column(columnDefinition="character varying(2048)")
    private String nameEn;

    @NotBlank
    @Column(columnDefinition="character varying(2048)")
    private String posterUrl;

    @NotBlank
    @Column(columnDefinition="character varying(2048)")
    private String trailerUrl;

    @NotBlank
    @Column(columnDefinition="TEXT")
    private String description;

    @NotBlank
    @Column(columnDefinition="character varying(2048)")
    private String slogan;

    @PastOrPresent
    private String year;

    @NotBlank
    @Size(max = 1000)
    private int movieLength;

    @ManyToMany
    private List<Genre> genres;

    @ManyToMany
    private List<Country> countries;

    @NotBlank
    @Size(max = 10)
    private double ratingImdb;

    @NotBlank
    @Size(max = 10)
    private double ratingKinopoisk;

    @JsonIgnore
    @NotBlank
    @Size
    private double votesScore;

    @JsonIgnore
    @NotBlank
    @Size
    private double numberOfVotes;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Review> reviews;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> voteUsers;

    @OneToOne
    public Type type;

    @Size(max = 10)
    public double getRating() {
        if ((this.votesScore == 0) || (this.numberOfVotes == 0)) return 0;
        return this.votesScore / this.numberOfVotes;
    }
}

