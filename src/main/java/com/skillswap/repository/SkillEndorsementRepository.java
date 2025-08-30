package com.skillswap.repository;

import com.skillswap.model.SkillEndorsement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillEndorsementRepository extends JpaRepository<SkillEndorsement, Long> {

    // Custom method to find all endorsements received by a specific user
    List<SkillEndorsement> findByEndorsedUserId(Long endorsedUserId);

    // Custom method to check if a user has already endorsed another user for a specific skill
    Optional<SkillEndorsement> findByEndorserIdAndEndorsedUserIdAndSkillId(Long endorserId, Long endorsedUserId, Long skillId);
}
