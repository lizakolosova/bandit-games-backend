package be.kdg.common.events;

import java.time.LocalDateTime;

public record MatchUpdatedEvent(
        LocalDateTime occurredAt,
        String gameId,
        String whitePlayer,
        String blackPlayer,
        String currentFen,
        String status,
        String updateType,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {
    public MatchUpdatedEvent(String gameId, String whitePlayer, String blackPlayer, String currentFen,
                             String status, String updateType, String messageType, LocalDateTime timestamp){
        this(LocalDateTime.now(), gameId, whitePlayer, blackPlayer, currentFen, status, updateType, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}
