package be.kdg.platform.port.in;

import java.time.LocalDate;
import java.util.List;

public record AddGameWithAchievementsCommand(
        String name,
        String rules,
        String pictureUrl,
        String gameUrl,
        String category,
        String developedBy,
        LocalDate createdAt,
        int averageMinutes,
        List<AchievementDefinitionCommand> achievements
) {}

