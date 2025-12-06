package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TicTacToeGameUpdatedProjectionCommand(
        UUID matchId,
        UUID playerId,
        int moveNumber,
        int position,
        List<String> boardState,
        LocalDateTime timestamp
) {}

