package com.moviescloud.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(max = 2500)
    private String title;

    @NotBlank
    @Size(max = 640000)
    @Column(columnDefinition="TEXT")
    private String description;

    @PastOrPresent
    private Date createdAt;

    @JsonIgnore
    @ManyToOne
    @NotNull
    private User author;
}
