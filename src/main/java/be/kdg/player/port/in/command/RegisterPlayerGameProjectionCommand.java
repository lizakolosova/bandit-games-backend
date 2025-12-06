package be.kdg.player.port.in.command;

public record RegisterPlayerGameProjectionCommand(
        String gameId,
        String name,
        String pictureUrl,
        String category,
        String rules,
        int achievementCount,
        int averageMinutes,
        String developedBy
) {}

