package be.kdg.common.events;

import java.time.LocalDateTime;

public record MoveMadeEvent(
        LocalDateTime occurredAt,
        String gameId,
        String fromSquare,
        String toSquare,
        String sanNotation,
        String fenAfterMove,
        String player,
        Integer moveNumber,
        String whitePlayer,
        String blackPlayer,
        LocalDateTime moveTime,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {
    public MoveMadeEvent (String gameId, String fromSquare, String toSquare, String sanNotation, String fenAfterMove,
                           String player, Integer moveNumber, String whitePlayer, String blackPlayer, LocalDateTime moveTime,
                           String messageType, LocalDateTime timestamp){
        this(LocalDateTime.now(), gameId, fromSquare, toSquare, sanNotation, fenAfterMove, player, moveNumber,  whitePlayer,
                blackPlayer, moveTime, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}