package com.skillswap.repository;

import com.skillswap.model.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

    // Custom method to find all reviews written about a specific user
    List<UserReview> findByReviewedUserId(Long reviewedUserId);

    // Custom method to find all reviews written by a specific user
    List<UserReview> findByReviewerId(Long reviewerId);
}
