package be.kdg.platform.port.in.command;

import be.kdg.common.events.chess.AchievementEntry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RegisterChessGameProjectionCommand(
        UUID registrationId,
        String frontendUrl,
        String pictureUrl,
        List<AchievementEntry> achievements,
        LocalDateTime timestamp
) {}
