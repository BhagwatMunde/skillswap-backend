package com.skillswap.repository;

import com.skillswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    /**
     * Finds all users who are offering to teach a specific skill.
     */
    @Query("SELECT u FROM User u JOIN u.userSkills us WHERE us.skill.id = :skillId AND us.offering = true")
    List<User> findUsersByOfferingSkill(@Param("skillId") Long skillId);
    
    /**
     * --- NEW ---
     * Finds all users who want to learn a specific skill.
     */
    @Query("SELECT u FROM User u JOIN u.userSkills us WHERE us.skill.id = :skillId AND us.offering = false")
    List<User> findUsersByLearningSkill(@Param("skillId") Long skillId);
}
