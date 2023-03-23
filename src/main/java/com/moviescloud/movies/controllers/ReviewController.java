package com.moviescloud.movies.controllers;

import com.moviescloud.movies.dto.ReviewDto;
import com.moviescloud.movies.entities.Movie;
import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.entities.Review;
import com.moviescloud.movies.entities.User;
import com.moviescloud.movies.services.IMovieService;
import com.moviescloud.movies.services.IReviewService;
import com.moviescloud.movies.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Tag(name = "Reviews", description = "Набор методов для работы с отзывами для фильмов.")
@RestController
@RequestMapping("/api/v1/movies")
public class ReviewController {

    final IReviewService reviewService;
    final IMovieService movieService;
    final IUserService userService;

    @Autowired
    public ReviewController(IReviewService reviewService, IMovieService movieService, IUserService userService) {
        this.reviewService = reviewService;
        this.movieService = movieService;
        this.userService = userService;
    }

    @Operation(summary = "Получить список рецензий (комментариев) к фильму по его идентификатору",
            description = "Возвращает список рецензий.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Review.class))
                            )
                    }
            )
    })
    @GetMapping("/{id}/reviews")
    public Response<Review> getAllReviewByMovieId(@PathVariable Long id) {
        List<Review> reviews = movieService.findById(id).getReviews();
        return new Response<>(HttpStatus.OK, reviews, reviews.size(), 1);
    }

    @Operation(summary = "Получить информацию об авторе рецензии (комментария) к фильму по идентификатору рецензии",
            description = "Возвращает информацию об авторе рецензии.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    }
            )
    })
    @GetMapping("/reviews/{id}/authors")
    public User getAuthorReviewByIdReview(@PathVariable Long id) {
        return userService.findById(reviewService.findById(id).getAuthor().getId());
    }

    @Operation(summary = "Добавить рецензию к фильму",
            description = "Добавляет рецензию (комментарий) к фильму (по его идентификатору).")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Review.class)
                            )
                    }
            )
    })
    @PostMapping("/{id}/reviews")
    public Review addReviewToMovie(
            @PathVariable Long id,
            @RequestBody ReviewDto reviewDto) {
        Movie movie = movieService.findById(id);

        Review review = new Review();
        review.setAuthor((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        review.setTitle(reviewDto.getTitle());
        review.setDescription(reviewDto.getDescription());
        review.setCreatedAt(new Date());

        List<Review> reviews = movie.getReviews();
        reviews.add(review);

        movie.setReviews(reviews);
        movieService.save(movie);
        return review;
    }

    @Operation(summary = "Удалить рецензию у фильма.",
            description = "Удаляет рецензию (комментарий) к фильму (по его идентификатору).")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Review.class)
                            )
                    }
            )
    })
    @DeleteMapping("/{id}/reviews")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable long id, @RequestBody Review review) {
        Movie movie = movieService.findById(id);
        for (Review r : movie.getReviews()) {
            if (r.getId() == review.getId()) {
                review = r;
            }
        }
        movie.getReviews().remove(review);
        movieService.save(movie);
        reviewService.delete(review);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Изменить данные в рецензии фильма.",
            description = "Позволяет редактировать информацию о рецензии (комментария) к фильму.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен успешно",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Review.class)
                            )
                    }
            )
    })
    @PutMapping("/reviews")
    public Review editReviewToMovie(@RequestBody Review review) {
        review.setAuthor(userService.findById(review.getAuthor().getId()));
        reviewService.save(review);
        return review;
    }
}
