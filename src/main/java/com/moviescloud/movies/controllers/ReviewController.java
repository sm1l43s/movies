package com.moviescloud.movies.controllers;

import com.moviescloud.movies.entities.Movie;
import com.moviescloud.movies.entities.Response;
import com.moviescloud.movies.entities.Review;
import com.moviescloud.movies.entities.User;
import com.moviescloud.movies.services.IMovieService;
import com.moviescloud.movies.services.IReviewService;
import com.moviescloud.movies.services.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}/reviews")
    public Response<Review> getAllReviewByMovieId(@PathVariable Long id) {
        List<Review> reviews = movieService.findById(id).getReviews();
        return new Response<>(HttpStatus.OK, reviews, reviews.size(), 1);
    }

    @GetMapping("/reviews/{id}/authors")
    public User getAuthorReviewByIdReview(@PathVariable Long id) {
        return userService.findById(reviewService.findById(id).getAuthor().getId());
    }

    @PostMapping("/{id}/reviews")
    public Review addReviewToMovie(
            @PathVariable Long id,
            @RequestBody Review review) {
        Movie movie = movieService.findById(id);

        review.setAuthor((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        List<Review> reviews = movie.getReviews();
        reviews.add(review);

        movie.setReviews(reviews);
        movieService.save(movie);
        return review;
    }

    @DeleteMapping("/{id}/reviews")
    public ResponseEntity<?> deleteReview(@PathVariable long id, @RequestBody Review review) {
        Movie movie = movieService.findById(id);
       for (Review r: movie.getReviews()) {
            if (r.getId() == review.getId()) {
                review = r;
            }
        }
        movie.getReviews().remove(review);
        movieService.save(movie);
        reviewService.delete(review);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/reviews")
    public Review editReviewToMovie(@RequestBody Review review) {
        review.setAuthor(userService.findById(review.getAuthor().getId()));
        reviewService.save(review);
        return review;
    }
}
