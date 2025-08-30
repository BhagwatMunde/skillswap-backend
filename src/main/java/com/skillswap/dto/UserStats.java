package com.skillswap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//This holds statistics for a specific user
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStats {
 private long sessionsTaught;
 private long sessionsLearned;
 private long skillsOffered;
 private long skillsWanted;
}
