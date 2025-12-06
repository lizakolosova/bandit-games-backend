package be.kdg.player.port.in.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChessAchievementUnlockedProjectionCommand(
        UUID playerId,
        UUID achievementId,
        UUID gameId,
        String achievementType,
        LocalDateTime timestamp
) {}