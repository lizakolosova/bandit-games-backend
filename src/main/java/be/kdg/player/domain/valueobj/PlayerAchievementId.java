package be.kdg.player.domain.valueobj;

import java.util.UUID;

public record PlayerAchievementId(UUID uuid) {
    public static PlayerAchievementId of(UUID uuid) {
        return new PlayerAchievementId(uuid);
    }
    public static PlayerAchievementId create() {
        return new PlayerAchievementId(UUID.randomUUID());
    }
}
