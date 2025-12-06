package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;

public record TicTacToeGameEndedProjectionCommand(String matchId, String winnerId, String endReason,
        int totalMoves, LocalDateTime timestamp) {}