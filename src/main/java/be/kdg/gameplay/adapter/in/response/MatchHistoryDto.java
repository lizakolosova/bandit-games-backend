package be.kdg.gameplay.adapter.in.response;

import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MatchHistoryDto(UUID matchId, UUID gameId, List<UUID> players, MatchStatus status,
        LocalDateTime startedAt, LocalDateTime finishedAt, UUID winnerPlayerId) {
    public static MatchHistoryDto from(Match match) {
        return new MatchHistoryDto(
                match.getMatchId().uuid(),
                match.getGameId().uuid(),
                match.getPlayers().stream().map(PlayerId::uuid).toList(),
                match.getStatus(),
                match.getStartedAt(),
                match.getFinishedAt(),
                match.getWinnerPlayerId() != null ? match.getWinnerPlayerId().uuid() : null
        );
    }
}
