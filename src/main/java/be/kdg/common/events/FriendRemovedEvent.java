package be.kdg.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record FriendRemovedEvent(LocalDateTime occurredAt,
                                 UUID playerId,
                                 UUID friendId) implements DomainEvent {
    public FriendRemovedEvent(UUID playerId, UUID friendId) {
        this(LocalDateTime.now(), playerId, friendId);
    }
    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}

