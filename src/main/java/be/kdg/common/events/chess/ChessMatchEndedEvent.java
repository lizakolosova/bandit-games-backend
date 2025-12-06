package be.kdg.common.events.chess;

import be.kdg.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChessMatchEndedEvent(
        String gameId,
        UUID whitePlayerId,
        String whitePlayerName,
        UUID blackPlayerId,
        String blackPlayerName,
        String finalFen,
        String endReason,
        String winner,
        Integer totalMoves,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    @Override
    public LocalDateTime eventPit() {
        return timestamp;
    }
}
