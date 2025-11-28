package be.kdg.gameplay.domain;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.valueobj.GameId;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.valueobj.MatchStatus;
import be.kdg.common.exception.MatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Match {

    private MatchId matchId;
    private GameId gameId;
    private List<PlayerId> players;

    private MatchStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private PlayerId winnerPlayerId;


    public Match(MatchId matchId, GameId gameId, List<PlayerId> players, MatchStatus status, LocalDateTime startedAt, LocalDateTime finishedAt, PlayerId winnerPlayerId) {
        this.matchId = matchId;
        this.gameId = gameId;
        this.players = players;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.winnerPlayerId = winnerPlayerId;
    }

    public Match(MatchId matchId, GameId gameId, List<PlayerId> players) {
        this.matchId = matchId;
        this.gameId = gameId;
        this.players = players;
        this.status = MatchStatus.IN_PROGRESS;
    }

    public MatchId getMatchId() {
        return matchId;
    }

    public GameId getGameId() {
        return gameId;
    }

    public List<PlayerId> getPlayers() {
        return players;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public PlayerId getWinnerPlayerId() {
        return winnerPlayerId;
    }
}

