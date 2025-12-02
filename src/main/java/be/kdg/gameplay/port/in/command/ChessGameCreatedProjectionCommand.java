package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;

public record ChessGameCreatedProjectionCommand(
        String gameId,
        String whitePlayer,
        String blackPlayer,
        LocalDateTime timestamp
) {}
