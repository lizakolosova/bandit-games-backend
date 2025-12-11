package be.kdg.player.port.in.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdatePlayerStatisticsCommand(UUID playerId, UUID gameId, UUID winnerId,
        LocalDateTime startedAt, LocalDateTime finishedAt) { }