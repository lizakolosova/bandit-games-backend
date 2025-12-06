package be.kdg.common.events.chess;

import be.kdg.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public record AchievementAcquiredEvent(
        UUID gameId,
        UUID playerId,
        String playerName,
        String achievementType,
        String achievementDescription,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    @Override
    public LocalDateTime eventPit() {
        return timestamp;
    }
}