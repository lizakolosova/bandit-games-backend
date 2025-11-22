package be.kdg.gameplay.adapter.out.mapper;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.adapter.out.MatchJpaEntity;
import be.kdg.gameplay.domain.Match;
import be.kdg.gameplay.domain.valueobj.MatchId;

import java.util.stream.Collectors;

public class MatchJpaMapper {

    public static MatchJpaEntity toEntity(Match match) {
        return new MatchJpaEntity(
                match.getMatchId().uuid(),
                match.getGameId().uuid(),
                match.getPlayers().stream()
                        .map(PlayerId::uuid)
                        .collect(Collectors.toSet()),
                match.getStatus(),
                match.getStartedAt(),
                match.getFinishedAt(),
                match.getWinnerPlayerId() == null ? null : match.getWinnerPlayerId().uuid()
        );
    }

    public static Match toDomain(MatchJpaEntity entity) {
        return new Match(
                MatchId.of(entity.getMatchId()),
                new GameId(entity.getGameId()),
                entity.getPlayers().stream()
                        .map(PlayerId::of)
                        .toList(),
                entity.getStatus(),
                entity.getStartedAt(),
                entity.getFinishedAt(),
                entity.getWinnerPlayerId() == null ? null : PlayerId.of(entity.getWinnerPlayerId())
        );
    }
}

