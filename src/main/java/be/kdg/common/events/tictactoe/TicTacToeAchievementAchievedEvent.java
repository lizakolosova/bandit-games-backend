package be.kdg.common.events.tictactoe;

import be.kdg.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicTacToeAchievementAchievedEvent(
        UUID playerId,
        UUID achievementId,
        String achievementType,
        UUID matchId,
        UUID gameId,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    @Override
    public LocalDateTime eventPit() {
        return timestamp;
    }
}