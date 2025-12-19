package be.kdg.common.events.chess;

import be.kdg.common.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ChessGameRegisteredEvent(
        UUID registrationId,
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