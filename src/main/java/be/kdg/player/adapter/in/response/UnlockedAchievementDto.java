package be.kdg.player.adapter.in.response;

import java.time.LocalDateTime;

public record UnlockedAchievementDto(
        String title,
        String description,
        int percentage,
        LocalDateTime unlockedAt
) {}

