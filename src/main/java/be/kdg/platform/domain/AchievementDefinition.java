package be.kdg.platform.domain;

import be.kdg.common.exception.InvalidRowException;
import be.kdg.common.valueobj.AchievementId;

public class AchievementDefinition {

    private AchievementId achievementId;
    private String name;
    private String description;

    public AchievementDefinition(AchievementId achievementId, String name, String description) {

        if (name == null || name.isBlank())
            throw new InvalidRowException("Achievement name cannot be empty");

        this.achievementId = achievementId;
        this.name = name;
        this.description = description;
    }

    public AchievementId getAchievementId() {
        return achievementId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

