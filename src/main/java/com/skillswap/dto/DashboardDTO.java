package com.skillswap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// This will be the main container for all dashboard data
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private UserStats userStats;
    private List<SkillStat> mostInDemandSkills;
}
