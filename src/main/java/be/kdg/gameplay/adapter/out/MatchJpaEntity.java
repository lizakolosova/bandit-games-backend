package be.kdg.gameplay.adapter.out;

import be.kdg.gameplay.domain.valueobj.MatchStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
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
    @Column(name = "player_id")
    private Set<UUID> players;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private UUID winnerPlayerId;

    protected MatchJpaEntity() {}

    public MatchJpaEntity(UUID matchId, UUID gameId, Set<UUID> players, MatchStatus status, LocalDateTime startedAt, LocalDateTime finishedAt, UUID winnerPlayerId) {
        this.matchId = matchId;
        this.gameId = gameId;
        this.players = players;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.winnerPlayerId = winnerPlayerId;
    }

    public UUID getMatchId() { return matchId; }
    public UUID getGameId() { return gameId; }
    public java.util.Set<UUID> getPlayers() { return players; }
    public MatchStatus getStatus() { return status; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public UUID getWinnerPlayerId() { return winnerPlayerId; }
}