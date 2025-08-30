package com.skillswap.repository;

import com.skillswap.model.SkillTestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillTestResultRepository extends JpaRepository<SkillTestResult, Long> {

    // Custom method to find all test results for a specific user
    List<SkillTestResult> findByUserId(Long userId);

    // Custom method to find a specific test result for a user
    Optional<SkillTestResult> findByUserIdAndSkillTestId(Long userId, Long skillTestId);
}
