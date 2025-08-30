package com.skillswap.service;

import com.skillswap.dto.UserSkillDTO;
import com.skillswap.model.User;
import com.skillswap.model.Skill;
import com.skillswap.model.UserSkill;
import com.skillswap.model.UserSkillId;
import com.skillswap.repository.UserRepository;
import com.skillswap.repository.SkillRepository;
import com.skillswap.repository.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSkillService {

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    /**
     * FIX: This method now correctly returns a List of UserSkillDTOs.
     * It fetches the UserSkill entities from the database and then uses a stream
     * to map each entity to a new UserSkillDTO object.
     */
    public List<UserSkillDTO> getUserSkills(Long userId) {
        return userSkillRepository.findByUserId(userId)
                .stream()
                .map(UserSkillDTO::new) // This is the conversion step
                .collect(Collectors.toList());
    }

    @Transactional
    public UserSkill addSkill(Long userId, Long skillId, boolean offering) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found with ID: " + skillId));

        if (userSkillRepository.findByUserIdAndSkillId(userId, skillId).isPresent()) {
            throw new RuntimeException("User already has this skill.");
        }

        UserSkill userSkill = new UserSkill();
        
        UserSkillId userSkillId = new UserSkillId();
        userSkillId.setUserId(userId);
        userSkillId.setSkillId(skillId);
        userSkill.setId(userSkillId);

        userSkill.setUser(user);
        userSkill.setSkill(skill);
        userSkill.setOffering(offering);

        return userSkillRepository.save(userSkill);
    }

    @Transactional
    public void removeSkill(Long userId, Long skillId) {
        UserSkillId id = new UserSkillId();
        id.setUserId(userId);
        id.setSkillId(skillId);

        if (!userSkillRepository.existsById(id)) {
            throw new RuntimeException("User does not have this skill.");
        }
        userSkillRepository.deleteById(id);
    }
}
