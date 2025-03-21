package com.moviescloud.movies.services;

import com.moviescloud.movies.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReviewService {
    Page<Review> findAll(Pageable pageable);

    Review findById(Long id);

    Review save(Review review);

    void delete(Review review);
}
