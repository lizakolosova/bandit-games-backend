package be.kdg.common.events.tictactoe;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;

public record TicTacToeMatchCreatedEvent(
        LocalDateTime occurredAt,
        String matchId,
        String hostPlayerId,
        String opponentPlayerId,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public TicTacToeMatchCreatedEvent(String matchId, String hostPlayerId, String opponentPlayerId, String messageType,
            LocalDateTime timestamp) {
        this(LocalDateTime.now(), matchId, hostPlayerId, opponentPlayerId, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}
