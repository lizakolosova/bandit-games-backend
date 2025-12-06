package be.kdg.player.port.in.command;

import java.util.UUID;

public record RegisterPlayerGameProjectionCommand(
        UUID gameId,
        String name,
        String pictureUrl,
        String category,
        String rules,
        int achievementCount,
        int averageMinutes,
        String developedBy
) {}

