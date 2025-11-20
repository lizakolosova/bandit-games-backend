package be.kdg.platform.domain;

import be.kdg.common.exception.InvalidRowException;
import be.kdg.common.valueobj.AchievementId;

public class AchievementDefinition {

    private AchievementId achievementId;
    private String name;
    private String description;
    private String howToUnlock;

    public AchievementDefinition(AchievementId achievementId,
                                 String name,
                                 String description,
                                 String howToUnlock) {

        if (name == null || name.isBlank())
            throw new InvalidRowException("Achievement name cannot be empty");

        this.achievementId = achievementId;
        this.name = name;
        this.description = description;
        this.howToUnlock = howToUnlock;
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

    public String getHowToUnlock() {
        return howToUnlock;
    }
}

