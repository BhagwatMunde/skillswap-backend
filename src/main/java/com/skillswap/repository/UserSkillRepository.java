package com.skillswap.repository;

import com.skillswap.model.UserSkill;
import com.skillswap.model.UserSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.skillswap.dto.SkillStat;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, UserSkillId> {
    List<UserSkill> findByUserId(Long userId);

    Optional<UserSkill> findByUserIdAndSkillId(Long userId, Long skillId);
    
    // --- NEW: Custom query for analytics ---
    // Counts how many users want to learn each skill
    @Query("SELECT new com.skillswap.dto.SkillStat(s.name, COUNT(us.id)) " +
           "FROM UserSkill us JOIN us.skill s " +
           "WHERE us.offering = false " +
           "GROUP BY s.name ORDER BY COUNT(us.id) DESC")
    List<SkillStat> findMostInDemandSkills();
    
    // --- NEW: Methods to count skills for a user's stats ---
    long countByUserIdAndOffering(Long userId, boolean offering);
}
