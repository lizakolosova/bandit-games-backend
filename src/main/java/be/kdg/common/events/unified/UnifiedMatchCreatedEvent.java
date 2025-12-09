package be.kdg.common.events.unified;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UnifiedMatchCreatedEvent(
        LocalDateTime occurredAt,
        UUID matchId,
        UUID gameId,
        String gameType,
        List<UUID> playerIds,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public UnifiedMatchCreatedEvent(UUID matchId, UUID gameId, String gameType,
                                    List<UUID> playerIds, String messageType, LocalDateTime timestamp) {
        this(LocalDateTime.now(), matchId, gameId, gameType, playerIds, messageType, timestamp);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}