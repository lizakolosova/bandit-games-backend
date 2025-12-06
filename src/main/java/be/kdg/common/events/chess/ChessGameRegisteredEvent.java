package be.kdg.common.events.chess;

import be.kdg.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.List;

public record ChessGameRegisteredEvent(
        String registrationId,
        String frontendUrl,
        String pictureUrl,
        List<AchievementEntry> availableAchievements,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    @Override
    public LocalDateTime eventPit() {
        return timestamp;
    }
}