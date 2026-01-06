package be.kdg.gameplay.adapter.out.dto;

import java.time.Instant;
import java.util.UUID;

public record MatchStartedEventDto(
        String type,
        UUID gameRoomId,
        UUID matchId,
        Instant timestamp
) {
    public MatchStartedEventDto(UUID gameRoomId, UUID matchId) {
        this("MATCH_STARTED", gameRoomId, matchId, Instant.now());
    }
}
