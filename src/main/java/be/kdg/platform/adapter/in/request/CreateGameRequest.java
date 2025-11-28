package be.kdg.platform.adapter.in.request;

import java.time.LocalDate;
import java.util.List;

public record CreateGameRequest(
        String name,
        String rules,
        String pictureUrl,
        String gameUrl,
        String category,
        String developedBy,
        LocalDate createdAt,
        int averageMinutes,
        List<AchievementDefinitionRequest> achievements
) {}
