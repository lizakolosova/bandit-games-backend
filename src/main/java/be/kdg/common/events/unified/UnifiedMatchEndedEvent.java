package be.kdg.common.events.unified;

import be.kdg.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;


public record UnifiedMatchEndedEvent(
        LocalDateTime occurredAt,
        UUID matchId,
        UUID winnerId,
        String endReason,
        int totalMoves,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public UnifiedMatchEndedEvent(UUID matchId, UUID winnerId, String endReason,
                                  int totalMoves, String messageType, LocalDateTime timestamp) {
        this(LocalDateTime.now(), matchId, winnerId, endReason, totalMoves, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}
