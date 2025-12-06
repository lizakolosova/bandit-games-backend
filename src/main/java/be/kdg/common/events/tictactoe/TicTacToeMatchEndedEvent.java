package be.kdg.common.events.tictactoe;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TicTacToeMatchEndedEvent(
        LocalDateTime occurredAt,
        UUID matchId,
        UUID winnerId,
        String endReason,
        int totalMoves,
        List<String> finalBoardState,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public TicTacToeMatchEndedEvent(UUID matchId, UUID winnerId, String endReason, int totalMoves, List<String> finalBoardState,
                                    String messageType, LocalDateTime timestamp) {
        this(LocalDateTime.now(), matchId, winnerId, endReason, totalMoves, finalBoardState, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}