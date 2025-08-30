package com.skillswap.controller;

import com.skillswap.model.User;
import com.skillswap.model.UserReview;
import com.skillswap.service.ReviewService;
import com.skillswap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    // DTO for the request body to create a review
    static class ReviewRequest {
        public Long reviewedUserId;
        public Long skillId;
        public int rating;
        public String comment;
    }

    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        User user = userService.findByEmail(username);
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<UserReview> createReview(@RequestBody ReviewRequest request) {
        Long reviewerId = getAuthenticatedUserId();
        UserReview newReview = reviewService.createReview(
            reviewerId,
            request.reviewedUserId,
            request.skillId,
            request.rating,
            request.comment
        );
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserReview>> getReviewsForUser(@PathVariable Long userId) {
        List<UserReview> reviews = reviewService.getReviewsForUser(userId);
        return ResponseEntity.ok(reviews);
    }
}
