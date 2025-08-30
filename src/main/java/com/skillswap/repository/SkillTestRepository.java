package com.skillswap.repository;

import com.skillswap.model.SkillTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillTestRepository extends JpaRepository<SkillTest, Long> {

    // Custom method to find a test associated with a specific skill
    Optional<SkillTest> findBySkillId(Long skillId);
}
