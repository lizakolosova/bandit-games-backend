package be.kdg.player.adapter.in.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UnlockedAchievementDto(
        UUID achievementId,
        LocalDateTime unlockedAt
) {}

