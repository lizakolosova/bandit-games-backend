package be.kdg.gameplay.adapter.out;

import be.kdg.gameplay.domain.valueobj.MatchStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "match", schema = "kdg_gameplay")
public class MatchJpaEntity {

    @Id
    private UUID matchId;

    private UUID gameId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "match_players",
            schema = "kdg_gameplay",
            joinColumns = @JoinColumn(name = "match_id")
    )
    @OrderColumn(name = "player_order")
    @Column(name = "player_id")
    private List<UUID> players;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private int totalMoves;
    private String endReason;

    private UUID winnerPlayerId;

    protected MatchJpaEntity() {}

    public MatchJpaEntity(UUID matchId, List<UUID> players, UUID gameId, MatchStatus status, LocalDateTime startedAt, LocalDateTime finishedAt, int totalMoves, String endReason, UUID winnerPlayerId) {
        this.matchId = matchId;
        this.players = players;
        this.gameId = gameId;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.totalMoves = totalMoves;
        this.endReason = endReason;
        this.winnerPlayerId = winnerPlayerId;
    }

    public UUID getMatchId() { return matchId; }
    public UUID getGameId() { return gameId; }
    public List<UUID> getPlayers() { return players; }
    public MatchStatus getStatus() { return status; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public UUID getWinnerPlayerId() { return winnerPlayerId; }

    public int getTotalMoves() {
        return totalMoves;
    }

    public String getEndReason() {
        return endReason;
    }
}