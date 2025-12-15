package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UnifiedMatchCreatedProjectionCommand(
        UUID matchId,
        UUID gameId,
        String gameType,
        List<UUID> playerIds,
        LocalDateTime timestamp
) {}