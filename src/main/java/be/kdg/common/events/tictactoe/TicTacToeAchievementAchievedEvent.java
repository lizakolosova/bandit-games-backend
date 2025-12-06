package be.kdg.common.events.tictactoe;

import be.kdg.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicTacToeAchievementAchievedEvent(
        LocalDateTime occurredAt,
        UUID playerId,
        UUID achievementId,
        String achievementType,
        UUID matchId,
        UUID gameId,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public TicTacToeAchievementAchievedEvent(UUID playerId, UUID achievementId, String achievementType, UUID matchId,
            UUID gameId, String messageType, LocalDateTime timestamp) {
        this(LocalDateTime.now(), playerId, achievementId, achievementType, matchId, gameId, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}

