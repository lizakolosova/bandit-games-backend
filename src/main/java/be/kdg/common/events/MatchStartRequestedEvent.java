package be.kdg.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatchStartRequestedEvent(UUID matchId, UUID platformGameId, UUID hostPlayerId, UUID guestPlayerId,
        LocalDateTime occurredAt) implements DomainEvent {

    public MatchStartRequestedEvent(UUID matchId, UUID platformGameId, UUID hostPlayerId, UUID guestPlayerId) {
        this(matchId, platformGameId, hostPlayerId, guestPlayerId, LocalDateTime.now());
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}
