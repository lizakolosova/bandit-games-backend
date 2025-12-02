package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;

public record ChessGameEndedProjectionCommand(
        String gameId,
        String whitePlayer,
        String blackPlayer,
        String winner,
        String endReason,
        Integer totalMoves,
        LocalDateTime occurredAt
) {}
