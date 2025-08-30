package com.skillswap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//This represents a single skill and its popularity count
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillStat {
 private String skillName;
 private long demandCount; // How many users want to learn this skill
}
