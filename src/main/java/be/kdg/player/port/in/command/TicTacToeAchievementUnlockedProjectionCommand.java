package be.kdg.player.port.in.command;

import java.time.LocalDateTime;

public record TicTacToeAchievementUnlockedProjectionCommand(String playerId, String achievementId, String gameId,
        String achievementType, LocalDateTime timestamp) {}