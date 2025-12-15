package be.kdg.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public record FinalizeRoomEvent(
        LocalDateTime occurredAt,
        String hostPlayerName,
        String opponentPlayerName,
        UUID hostPlayerId,
        UUID opponentPlayerId
) implements DomainEvent {

    public FinalizeRoomEvent(String hostPlayerName, String opponentPlayerName, UUID hostPlayerId, UUID opponentPlayerId){
    this(LocalDateTime.now(), hostPlayerName, opponentPlayerName, hostPlayerId, opponentPlayerId);
    }

    @Override
    public LocalDateTime eventPit() {
        return occurredAt;
    }
}
