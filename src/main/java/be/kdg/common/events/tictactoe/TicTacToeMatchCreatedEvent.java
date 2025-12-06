package be.kdg.common.events.tictactoe;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicTacToeMatchCreatedEvent(
        LocalDateTime occurredAt,
        UUID matchId,
        UUID hostPlayerId,
        UUID opponentPlayerId,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public TicTacToeMatchCreatedEvent(UUID matchId, UUID hostPlayerId, UUID opponentPlayerId, String messageType,
            LocalDateTime timestamp) {
        this(LocalDateTime.now(), matchId, hostPlayerId, opponentPlayerId, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}
