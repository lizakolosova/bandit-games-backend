package be.kdg.common.events.tictactoe;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TicTacToeMatchMoveMadeEvent(
        UUID matchId,
        UUID gameId,
        UUID playerId,
        int position,
        String playerSymbol,
        int moveNumber,
        List<String> boardStateAfterMove,
        UUID hostPlayerId,
        UUID opponentPlayerId,
        LocalDateTime moveTime,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    @Override
    public LocalDateTime eventPit() {
        return timestamp;
    }
}