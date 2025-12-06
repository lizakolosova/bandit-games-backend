package be.kdg.platform.port.in.command;

import be.kdg.common.events.chess.AchievementEntry;

import java.time.LocalDateTime;
import java.util.List;

public record RegisterChessGameProjectionCommand(
        String registrationId,
        String frontendUrl,
        String pictureUrl,
        List<AchievementEntry> achievements,
        LocalDateTime timestamp
) {}
