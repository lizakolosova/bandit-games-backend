package be.kdg.common.events.unified;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;
import java.util.UUID;

public record UnifiedMatchUpdatedEvent(
        LocalDateTime occurredAt,
        UUID matchId,
        UUID player1,
        UUID player2,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public UnifiedMatchUpdatedEvent(UUID matchId, UUID whitePlayerId, UUID blackPlayerId,
                                    String messageType, LocalDateTime timestamp) {
        this(LocalDateTime.now(), matchId, whitePlayerId, blackPlayerId, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}