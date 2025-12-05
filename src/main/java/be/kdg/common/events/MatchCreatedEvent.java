package be.kdg.common.events;

import java.time.LocalDateTime;

public record MatchCreatedEvent(
        String gameId,
        String whitePlayer,
        String blackPlayer,
        String currentFen,
        String status,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {
    @Override
    public LocalDateTime eventPit() {
        return timestamp;
    }
}