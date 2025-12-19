package be.kdg.gameplay.domain;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.events.unified.MatchStatisticsEvent;
import be.kdg.common.valueobj.GameId;
import be.kdg.gameplay.domain.valueobj.MatchId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.valueobj.MatchStatus;

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

    private int totalMoves;
    private String endReason;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Match(MatchId matchId, GameId gameId, List<PlayerId> players, MatchStatus status, LocalDateTime startedAt, LocalDateTime finishedAt, PlayerId winnerPlayerId, int totalMoves, String endReason) {
        this.matchId = matchId;
        this.gameId = gameId;
        this.players = players;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.winnerPlayerId = winnerPlayerId;
        this.totalMoves = totalMoves;
        this.endReason = endReason;
    }

    public Match(MatchId matchId, GameId gameId, List<PlayerId> players, MatchStatus status, LocalDateTime startedAt) {
        this.matchId = matchId;
        this.gameId = gameId;
        this.players = players;
        this.status = status;
        this.startedAt = startedAt;
    }

    public void updatePlayers(PlayerId white, PlayerId black) {
        this.players = List.of(white, black);
    }

    public void finish(LocalDateTime finishedAt, PlayerId winner, String endReason, int totalMoves) {
        this.status = MatchStatus.FINISHED;
        this.finishedAt = finishedAt;
        this.winnerPlayerId = winner;
        this.totalMoves = totalMoves;
        this.endReason = endReason;
        registerEvent(new MatchStatisticsEvent(
                this.matchId.uuid(),
                this.gameId.uuid(),
                this.players.stream().map(PlayerId::uuid).toList(),
                winner != null ? winner.uuid() : null,
                this.startedAt,
                this.finishedAt));
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public void registerEvent(DomainEvent event) {
        domainEvents.add(event);
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

    public int getTotalMoves() {
        return totalMoves;
    }

    public String getEndReason() {
        return endReason;
    }
}

