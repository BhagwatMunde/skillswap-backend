package com.skillswap.service;

import com.skillswap.model.Skill;
import com.skillswap.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public Skill createSkill(Skill skill) {
        // Business logic: check for duplicate skills before saving
        if (skillRepository.findByName(skill.getName()).isPresent()) {
            throw new RuntimeException("Skill with this name already exists.");
        }
        return skillRepository.save(skill);
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    public Skill updateSkill(Long id, Skill skillDetails) {
        // Business logic: check if the skill exists before updating
        Skill existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found with ID: " + id));

        existingSkill.setName(skillDetails.getName());
        existingSkill.setDescription(skillDetails.getDescription());

        return skillRepository.save(existingSkill);
    }

    public void deleteSkill(Long id) {
        // Business logic: check if the skill exists before deleting
        if (!skillRepository.existsById(id)) {
            throw new RuntimeException("Skill not found with ID: " + id);
        }
        skillRepository.deleteById(id);
    }
}
