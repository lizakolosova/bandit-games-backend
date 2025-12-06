package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;

public record TicTacToeGameCreatedProjectionCommand(String gameId, String matchId, String hostPlayerId, String opponentPlayerId,
        LocalDateTime timestamp) {}