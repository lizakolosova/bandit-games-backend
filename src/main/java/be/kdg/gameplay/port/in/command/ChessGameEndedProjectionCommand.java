package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChessGameEndedProjectionCommand(
        UUID gameId,
        UUID whitePlayerId,
        UUID blackPlayerId,
        String winner,
        String endReason,
        Integer totalMoves,
        LocalDateTime occurredAt
) {}
