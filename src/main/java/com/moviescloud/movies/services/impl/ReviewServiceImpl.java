package com.moviescloud.movies.services.impl;

import com.moviescloud.movies.entities.Review;
import com.moviescloud.movies.exceptions.ResourceNotFoundException;
import com.moviescloud.movies.repositories.ReviewRepository;
import com.moviescloud.movies.services.IReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {

    final ReviewRepository reviewRepository;

    @Override
    public Page<Review> findAll(Pageable pageable) {
        log.info("Getting a list of reviews");
        return reviewRepository.findAll(pageable);
    }

    @Override
    public Review findById(Long id) {
        log.info("Get a review by id{}", id);
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review with id=" + id + " not found"));
    }

    @Override
    public Review save(Review review) {
        log.info("Save a review{}", review);
        return reviewRepository.save(review);
    }

    @Override
    public void delete(Review review) {
        log.info("Delete a review{}", review);
        reviewRepository.delete(review);
    }
}
