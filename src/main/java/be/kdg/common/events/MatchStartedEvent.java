package be.kdg.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatchStartedEvent(
        LocalDateTime occurredAt,
        UUID matchId,
        UUID gameId,
        UUID hostPlayerId,
        UUID opponentPlayerId
) implements DomainEvent{

    public MatchStartedEvent (UUID matchId, UUID gameId, UUID hostPlayerId, UUID opponentPlayerId){
    this(LocalDateTime.now(), matchId, gameId, hostPlayerId, opponentPlayerId);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}
