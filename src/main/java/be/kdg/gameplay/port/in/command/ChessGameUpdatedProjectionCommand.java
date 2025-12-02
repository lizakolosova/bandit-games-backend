package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;

public record ChessGameUpdatedProjectionCommand(
        String gameId,
        String whitePlayer,
        String blackPlayer,
        LocalDateTime timestamp
) {}