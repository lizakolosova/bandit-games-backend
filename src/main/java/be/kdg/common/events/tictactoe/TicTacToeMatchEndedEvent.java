package be.kdg.common.events.tictactoe;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TicTacToeMatchEndedEvent(
        UUID matchId,
        UUID gameId,
        UUID hostPlayerId,
        UUID opponentPlayerId,
        List<String> finalBoardState,
        String endReason,
        UUID winnerId,
        int totalMoves,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    @Override
    public LocalDateTime eventPit() {
        return timestamp;
    }
}