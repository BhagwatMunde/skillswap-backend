package com.skillswap.service;

import com.skillswap.model.SkillEndorsement;
import com.skillswap.repository.SkillEndorsementRepository;
import com.skillswap.repository.UserRepository;
import com.skillswap.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EndorsementService {

    @Autowired
    private SkillEndorsementRepository endorsementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    /**
     * Allows a user to endorse another user for a specific skill.
     * @param endorserId The ID of the user giving the endorsement.
     * @param endorsedUserId The ID of the user being endorsed.
     * @param skillId The ID of the skill being endorsed.
     * @return The saved SkillEndorsement object.
     */
    public SkillEndorsement addEndorsement(Long endorserId, Long endorsedUserId, Long skillId) {
        // Business rule: A user cannot endorse themselves.
        if (endorserId.equals(endorsedUserId)) {
            throw new RuntimeException("Users cannot endorse themselves.");
        }

        // Business rule: Prevent duplicate endorsements.
        endorsementRepository.findByEndorserIdAndEndorsedUserIdAndSkillId(endorserId, endorsedUserId, skillId)
            .ifPresent(e -> {
                throw new RuntimeException("You have already endorsed this user for this skill.");
            });

        SkillEndorsement endorsement = new SkillEndorsement();
        endorsement.setEndorser(userRepository.findById(endorserId)
                .orElseThrow(() -> new RuntimeException("Endorser not found")));
        endorsement.setEndorsedUser(userRepository.findById(endorsedUserId)
                .orElseThrow(() -> new RuntimeException("Endorsed user not found")));
        endorsement.setSkill(skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found")));

        return endorsementRepository.save(endorsement);
    }

    /**
     * Gets all endorsements for a specific user.
     * @param userId The ID of the user.
     * @return A list of endorsements.
     */
    public List<SkillEndorsement> getEndorsementsForUser(Long userId) {
        return endorsementRepository.findByEndorsedUserId(userId);
    }
}
