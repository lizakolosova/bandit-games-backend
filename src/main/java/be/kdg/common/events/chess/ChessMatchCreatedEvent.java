package be.kdg.common.events.chess;

import be.kdg.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChessMatchCreatedEvent(
        UUID gameId,
        UUID whitePlayerId,
        String whitePlayerName,
        UUID blackPlayerId,
        String blackPlayerName,
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