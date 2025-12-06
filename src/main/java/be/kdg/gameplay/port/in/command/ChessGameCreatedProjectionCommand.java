package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChessGameCreatedProjectionCommand(
        UUID matchId,
        String gameName,
        UUID whitePlayerId,
        UUID blackPlayerId,
        LocalDateTime timestamp
) {}
