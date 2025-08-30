package com.skillswap.repository;

import com.skillswap.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Skill entities.
 * Extends JpaRepository to get basic CRUD operations and adds a custom query method.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    /**
     * Custom query method to find a skill by its name.
     * @param name The name of the skill to find.
     * @return An Optional containing the found Skill, or an empty Optional if not found.
     */
    Optional<Skill> findByName(String name);
}
