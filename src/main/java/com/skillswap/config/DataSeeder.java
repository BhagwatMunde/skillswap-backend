package com.skillswap.config;

import com.skillswap.model.Role;
import com.skillswap.model.Skill;
import com.skillswap.repository.RoleRepository;
import com.skillswap.repository.SkillRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final SkillRepository skillRepository;

    public DataSeeder(RoleRepository roleRepository, SkillRepository skillRepository) {
        this.roleRepository = roleRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
        seedSkills();
    }

    private void seedRoles() {
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role("ROLE_USER"));
            System.out.println("Default ROLE_USER created.");
        }
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
            System.out.println("Default ROLE_ADMIN created.");
        }
    }

    private void seedSkills() {
        // --- FIX: This logic now checks for each skill individually ---
        // This ensures that if some skills already exist, the missing ones are still added.
        List<Skill> skillsToAdd = Arrays.asList(
            new Skill("Java", "Learn the fundamentals of Java programming.", "Programming", Skill.DifficultyLevel.INTERMEDIATE, null),
            new Skill("Python", "Master Python for data science and web development.", "Programming", Skill.DifficultyLevel.BEGINNER, null),
            new Skill("Guitar", "From basic chords to advanced techniques.", "Music", Skill.DifficultyLevel.BEGINNER, null),
            new Skill("Public Speaking", "Build confidence and deliver compelling presentations.", "Communication", Skill.DifficultyLevel.INTERMEDIATE, null),
            new Skill("Cooking", "Learn to cook delicious and healthy meals.", "Lifestyle", Skill.DifficultyLevel.BEGINNER, null),
            new Skill("React", "Build modern, interactive web applications.", "Programming", Skill.DifficultyLevel.ADVANCED, null),
            new Skill("Yoga", "Improve flexibility, strength, and mindfulness.", "Health", Skill.DifficultyLevel.BEGINNER, null)
        );

        for (Skill skill : skillsToAdd) {
            if (skillRepository.findByName(skill.getName()).isEmpty()) {
                skillRepository.save(skill);
                System.out.println("Added skill to database: " + skill.getName());
            }
        }
    }
}
