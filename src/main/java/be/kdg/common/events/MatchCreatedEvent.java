package be.kdg.common.events;

import java.time.LocalDateTime;

public record MatchCreatedEvent(
        LocalDateTime occurredAt,
        String gameId,
        String whitePlayer,
        String blackPlayer,
        String currentFen,
        String status,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {
    public MatchCreatedEvent(String gameId, String whitePlayer, String blackPlayer, String currentFen,
                             String status, String messageType, LocalDateTime timestamp){
        this(LocalDateTime.now(), gameId, whitePlayer, blackPlayer, currentFen, status, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}