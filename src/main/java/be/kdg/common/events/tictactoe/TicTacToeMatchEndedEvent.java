package be.kdg.common.events.tictactoe;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;
import java.util.List;

public record TicTacToeMatchEndedEvent(
        LocalDateTime occurredAt,
        String matchId,
        String winnerId,
        String endReason,
        int totalMoves,
        List<String> finalBoardState,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public TicTacToeMatchEndedEvent(String matchId, String winnerId, String endReason, int totalMoves, List<String> finalBoardState,
                                    String messageType, LocalDateTime timestamp) {
        this(LocalDateTime.now(), matchId, winnerId, endReason, totalMoves, finalBoardState, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}