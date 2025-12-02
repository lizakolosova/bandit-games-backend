package be.kdg.common.events;

import java.time.LocalDateTime;

public record MatchEndedEvent(
        LocalDateTime occurredAt,
        String gameId,
        String whitePlayer,
        String blackPlayer,
        String finalFen,
        String endReason,
        String winner,
        Integer totalMoves,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent{
    public MatchEndedEvent(String gameId, String whitePlayer, String blackPlayer, String finalFen,
                           String endReason, String winner, Integer totalMoves, String messageType, LocalDateTime timestamp){
        this(LocalDateTime.now(), gameId, whitePlayer, blackPlayer, finalFen, endReason, winner, totalMoves, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}
