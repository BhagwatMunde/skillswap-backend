package com.skillswap.service;

import com.skillswap.dto.DashboardDTO;
import com.skillswap.dto.SkillStat;
import com.skillswap.dto.UserStats;
import com.skillswap.model.Session;
import com.skillswap.repository.SessionRepository;
import com.skillswap.repository.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public DashboardDTO getDashboardData(Long userId) {
        // 1. Get user-specific stats
        long sessionsTaught = sessionRepository.countBySkillRequest_ReceiverIdAndStatus(userId, Session.SessionStatus.COMPLETED);
        long sessionsLearned = sessionRepository.countBySkillRequest_RequesterIdAndStatus(userId, Session.SessionStatus.COMPLETED);
        long skillsOffered = userSkillRepository.countByUserIdAndOffering(userId, true);
        long skillsWanted = userSkillRepository.countByUserIdAndOffering(userId, false);
        
        UserStats userStats = new UserStats(sessionsTaught, sessionsLearned, skillsOffered, skillsWanted);

        // 2. Get platform-wide stats
        List<SkillStat> mostInDemandSkills = userSkillRepository.findMostInDemandSkills();

        // 3. Combine into a single DTO
        return new DashboardDTO(userStats, mostInDemandSkills);
    }
}
