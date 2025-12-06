package be.kdg.common.events.tictactoe;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;
import java.util.List;

public record TicTacToeMatchUpdatedEvent(
        LocalDateTime occurredAt,
        String matchId,
        String playerId,
        int moveNumber,
        int position,
        List<String> boardStateAfterMove,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public TicTacToeMatchUpdatedEvent(String matchId, String playerId, int moveNumber, int position,
            List<String> boardStateAfterMove, String messageType, LocalDateTime timestamp) {
        this(LocalDateTime.now(), matchId, playerId, moveNumber, position, boardStateAfterMove, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}