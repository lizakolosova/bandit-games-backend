package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;
import java.util.List;

public record TicTacToeGameUpdatedProjectionCommand(
        String matchId,
        String playerId,
        int moveNumber,
        int position,
        List<String> boardState,
        LocalDateTime timestamp
) {}

