package be.kdg.gameplay;

import be.kdg.common.valueobj.GameId;
import be.kdg.gameplay.valueobj.GameState;
import be.kdg.gameplay.valueobj.MatchId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.valueobj.MatchStatus;

import java.time.LocalDateTime;
import java.util.List;

public class Match {

    private MatchId matchId;
    private GameId gameId;
    private List<PlayerId> players;

    private MatchStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private PlayerId winnerPlayerId;
    private GameState currentState;

    public Match(MatchId matchId, GameId gameId, List<PlayerId> players, MatchStatus status, LocalDateTime startedAt, LocalDateTime finishedAt, PlayerId winnerPlayerId, GameState currentState) {
        this.matchId = matchId;
        this.gameId = gameId;
        this.players = players;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.winnerPlayerId = winnerPlayerId;
        this.currentState = currentState;
    }

    public Match(MatchId matchId, GameId gameId, List<PlayerId> players) {
        this.matchId = matchId;
        this.gameId = gameId;
        this.players = players;
        this.status = MatchStatus.PENDING;
    }

    public void start(GameState initialState) {
        this.status = MatchStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
        this.currentState = initialState;
    }

    public void updateState(GameState newState) {
        if (status != MatchStatus.IN_PROGRESS)
            throw new IllegalStateException("Match not active");
        this.currentState = newState;
    }

    public void finish(PlayerId winner, GameState finalState) {
        this.status = MatchStatus.FINISHED;
        this.winnerPlayerId = winner;
        this.currentState = finalState;
        this.finishedAt = LocalDateTime.now();
    }
}

