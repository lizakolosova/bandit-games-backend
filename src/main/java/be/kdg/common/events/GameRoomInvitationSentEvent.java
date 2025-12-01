package be.kdg.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameRoomInvitationSentEvent(LocalDateTime occurredAt, UUID gameRoomId, UUID hostPlayerId, UUID invitedPlayerId)
        implements DomainEvent {
    public GameRoomInvitationSentEvent(UUID gameRoomId, UUID hostPlayerId, UUID invitedPlayerId) {
        this(LocalDateTime.now(), gameRoomId, hostPlayerId, invitedPlayerId);

    }
    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}
