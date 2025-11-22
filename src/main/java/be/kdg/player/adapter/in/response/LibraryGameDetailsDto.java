package be.kdg.player.adapter.in.response;

import be.kdg.platform.adapter.in.response.AchievementDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LibraryGameDetailsDto(
        UUID gameId,
        String name,
        String pictureUrl,
        String category,
        int achievementCount,
        int averageMinutes,
        String developedBy,

        LocalDateTime addedAt,
        LocalDateTime lastPlayedAt,
        long totalMinutes,
        boolean favourite,

        int unlockedCount,
        List<UnlockedAchievementDto> unlockedAchievements
) {}



