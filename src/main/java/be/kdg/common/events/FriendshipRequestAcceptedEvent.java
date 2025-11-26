package be.kdg.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record FriendshipRequestAcceptedEvent(
        LocalDateTime occurredAt,
        UUID friendshipRequestId,
        UUID senderId,
        UUID receiverId
) implements DomainEvent {
    public FriendshipRequestAcceptedEvent(UUID friendshipRequestId, UUID senderId, UUID receiverId) {
        this(LocalDateTime.now(), friendshipRequestId, senderId, receiverId);
    }
    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}

