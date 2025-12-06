package be.kdg.gameplay.port.in.command;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicTacToeGameCreatedProjectionCommand(UUID gameId, UUID matchId, UUID hostPlayerId, UUID opponentPlayerId,
                                                    LocalDateTime timestamp) {}