package be.kdg.common.events;

import java.time.LocalDateTime;
import java.util.List;

public record GameRegisteredEvent(
        LocalDateTime occurredAt,
        String registrationId,
        String frontendUrl,
        String pictureUrl,
        List<AchievementEntry> availableAchievements,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public GameRegisteredEvent(
            String registrationId,
            String frontendUrl,
            String pictureUrl,
            List<AchievementEntry> availableAchievements,
            String messageType,
            LocalDateTime timestamp
    ) {
        this(LocalDateTime.now(), registrationId, frontendUrl, pictureUrl, availableAchievements, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}