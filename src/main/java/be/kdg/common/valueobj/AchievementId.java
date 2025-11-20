package be.kdg.common.valueobj;

import java.util.UUID;

public record AchievementId(UUID uuid) {
    public static AchievementId of(UUID uuid) {
        return new AchievementId(uuid);
    }
    public static AchievementId create() {
        return new AchievementId(UUID.randomUUID());
    }
}
