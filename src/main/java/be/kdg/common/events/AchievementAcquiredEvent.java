package be.kdg.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record AchievementAcquiredEvent(
        LocalDateTime occurredAt,
        String gameId,
        String playerId,
        String playerName,
        String achievementType,
        String achievementDescription,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent{
    public AchievementAcquiredEvent (String gameId, String playerId, String playerName, String achievementType,
                                             String achievementDescription, String messageType, LocalDateTime timestamp){
        this(LocalDateTime.now(), gameId, playerId, playerName, achievementType, achievementDescription, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}