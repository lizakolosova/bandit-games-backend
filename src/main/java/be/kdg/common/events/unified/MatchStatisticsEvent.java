package be.kdg.common.events.unified;

import be.kdg.common.events.DomainEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MatchStatisticsEvent(
        UUID matchId,
        UUID gameId,
        List<UUID> playerIds,
        UUID winnerId,
        LocalDateTime startedAt,
        LocalDateTime finishedAt,
        String messageType,
        LocalDateTime timestamp
) implements DomainEvent {

    public MatchStatisticsEvent(UUID matchId, UUID gameId, List<UUID> playerIds,
                                UUID winnerId, LocalDateTime startedAt,
                                LocalDateTime finishedAt) {
        this(matchId, gameId, playerIds, winnerId, startedAt, finishedAt, "MatchStatistics", LocalDateTime.now());
    }

    @Override
    public LocalDateTime eventPit() {
        return timestamp;
    }
}