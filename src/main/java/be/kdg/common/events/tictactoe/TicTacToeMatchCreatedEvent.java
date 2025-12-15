package be.kdg.common.events.tictactoe;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TicTacToeMatchCreatedEvent(
        UUID matchId,
        UUID gameId,
        UUID hostPlayerId,
        UUID opponentPlayerId,
        List<String> currentBoardState,
        String status,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    @Override
    public LocalDateTime eventPit() {
        return timestamp;
    }
}
