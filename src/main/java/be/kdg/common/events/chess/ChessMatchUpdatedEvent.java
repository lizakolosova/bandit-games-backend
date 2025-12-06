package be.kdg.common.events.chess;

import be.kdg.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChessMatchUpdatedEvent(
        LocalDateTime occurredAt,
        String gameId,
        UUID whitePlayerId,
        String whitePlayerName,
        UUID blackPlayerId,
        String blackPlayerName,
        String currentFen,
        String status,
        String updateType,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {
    public ChessMatchUpdatedEvent(String gameId, UUID whitePlayerId, String whitePlayerName, UUID blackPlayerId,
                                  String blackPlayerName, String currentFen, String status, String updateType, String messageType,
                                  LocalDateTime timestamp){
        this(LocalDateTime.now(), gameId, whitePlayerId, whitePlayerName, blackPlayerId, blackPlayerName, currentFen,
                status, updateType, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}
