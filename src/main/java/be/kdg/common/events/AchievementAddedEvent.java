package be.kdg.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record AchievementAddedEvent(
        LocalDateTime occurredAt,
        UUID achievementId,
        UUID gameId,
        String name,
        String description
) implements DomainEvent {

    public AchievementAddedEvent(UUID achievementId,
                                 UUID gameId,
                                 String name,
                                 String description) {
        this(LocalDateTime.now(), achievementId, gameId, name, description);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}

