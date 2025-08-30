package com.skillswap.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

// This class must be public and in its own file
@Embeddable
public class UserSkillId implements Serializable {
    private Long userId;
    private Long skillId;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }

    @Override
    public int hashCode() {
        return (int) (userId + skillId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserSkillId that = (UserSkillId) obj;
        return userId.equals(that.userId) && skillId.equals(that.skillId);
    }
}
