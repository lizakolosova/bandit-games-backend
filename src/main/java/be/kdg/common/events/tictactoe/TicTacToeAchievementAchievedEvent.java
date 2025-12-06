package be.kdg.common.events.tictactoe;

import be.kdg.common.events.DomainEvent;

import java.time.LocalDateTime;

public record TicTacToeAchievementAchievedEvent(
        LocalDateTime occurredAt,
        String playerId,
        String achievementId,
        String achievementType,
        String matchId,
        String gameId,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public TicTacToeAchievementAchievedEvent(
            String playerId,
            String achievementId,
            String achievementType,
            String matchId,
            String gameId,
            String messageType,
            LocalDateTime timestamp
    ) {
        this(LocalDateTime.now(), playerId, achievementId, achievementType, matchId, gameId, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}

