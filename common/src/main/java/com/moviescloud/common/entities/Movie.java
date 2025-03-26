package com.moviescloud.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
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

