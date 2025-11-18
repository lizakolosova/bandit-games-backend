package be.kdg.platform.domain.valueobj;

import be.kdg.common.valueobj.AchievementId;
import be.kdg.platform.domain.AchievementDefinition;

import java.util.Map;
import java.util.Objects;

public record AchievementDefinitions(Map<AchievementId, AchievementDefinition> items) {

    public AchievementDefinitions {
        Objects.requireNonNull(items);
    }

    public void add(AchievementDefinition achievement) {
        items.put(achievement.getAchievementId(), achievement);
    }

    public void remove(AchievementId id) {
        items.remove(id);
    }

}