package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;

public record MatchCreatedCommand(
        String matchId,
        String gameId,
        String whitePlayer,
        String blackPlayer,
        String platformMatchId,
        LocalDateTime timestamp
) {}

