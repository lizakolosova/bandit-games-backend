package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicTacToeGameEndedProjectionCommand(UUID matchId, UUID winnerId, String endReason,
                                                  int totalMoves, LocalDateTime timestamp) {}