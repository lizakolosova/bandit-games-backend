package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChessGameUpdatedProjectionCommand(
        String gameId,
        UUID whitePlayerId,
        UUID blackPlayerId,
        LocalDateTime timestamp
) {}