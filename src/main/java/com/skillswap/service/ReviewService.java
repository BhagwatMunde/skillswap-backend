package com.skillswap.service;

import com.skillswap.model.UserReview;
import com.skillswap.repository.UserReviewRepository;
import com.skillswap.repository.UserRepository;
import com.skillswap.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private UserReviewRepository userReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    /**
     * Creates a new review.
     * @param reviewerId The ID of the user writing the review.
     * @param reviewedUserId The ID of the user being reviewed.
     * @param skillId The ID of the skill the review is about.
     * @param rating The rating score (e.g., 1-5).
     * @param comment The review comment.
     * @return The saved UserReview object.
     */
    public UserReview createReview(Long reviewerId, Long reviewedUserId, Long skillId, int rating, String comment) {
        UserReview review = new UserReview();
        review.setReviewer(userRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found")));
        review.setReviewedUser(userRepository.findById(reviewedUserId)
                .orElseThrow(() -> new RuntimeException("Reviewed user not found")));
        review.setSkill(skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found")));
        review.setRating(rating);
        review.setComment(comment);

        return userReviewRepository.save(review);
    }

    /**
     * Gets all reviews for a specific user.
     * @param userId The ID of the user whose reviews are to be fetched.
     * @return A list of reviews.
     */
    public List<UserReview> getReviewsForUser(Long userId) {
        return userReviewRepository.findByReviewedUserId(userId);
    }
}
