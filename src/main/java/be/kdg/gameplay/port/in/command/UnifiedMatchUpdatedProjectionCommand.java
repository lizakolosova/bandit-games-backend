package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record UnifiedMatchUpdatedProjectionCommand(
        UUID matchId,
        UUID playerId1,
        UUID playerId2,
        LocalDateTime timestamp
) {}