package be.kdg.common.events.unified;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;
import java.util.UUID;

public record UnifiedAchievementAchievedEvent(
        LocalDateTime occurredAt,
        UUID playerId,
        UUID achievementId,
        String achievementType,
        UUID matchId,
        String gameType,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public UnifiedAchievementAchievedEvent(UUID playerId, UUID achievementId, String achievementType,
                                           UUID matchId, String gameType, String messageType, LocalDateTime timestamp) {
        this(LocalDateTime.now(), playerId, achievementId, achievementType, matchId, gameType, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}