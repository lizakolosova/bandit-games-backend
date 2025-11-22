package be.kdg.player.port.in;

import java.time.LocalDate;
import java.util.UUID;

public record GameAddedProjectionCommand(
        UUID gameId,
        String name,
        String rules,
        String pictureUrl,
        String category,
        String developedBy,
        LocalDate createdAt,
        int achievementCount,
        int averageMinutes
) {}