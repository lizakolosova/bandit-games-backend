package be.kdg.player.adapter.out;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "player_game_library", schema = "kdg_player")
public class GameLibraryJpaEntity {

    @Id
    private UUID id;

    private UUID gameId;

    private LocalDateTime addedAt;
    private LocalDateTime lastPlayedAt;

    private Long totalPlaytimeSeconds;

    private boolean favourite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerJpaEntity player;

    protected GameLibraryJpaEntity() {}

    public GameLibraryJpaEntity(UUID id, PlayerJpaEntity player, UUID gameId) {
        this.id = id;
        this.player = player;
        this.gameId = gameId;
        this.addedAt = LocalDateTime.now();
        this.totalPlaytimeSeconds = 0L;
        this.favourite = false;
    }

    public UUID getId() {
        return id;
    }

    public UUID getGameId() {
        return gameId;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public LocalDateTime getLastPlayedAt() {
        return lastPlayedAt;
    }

    public Long getTotalPlaytimeSeconds() {
        return totalPlaytimeSeconds;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public PlayerJpaEntity getPlayer() {
        return player;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public void setLastPlayedAt(LocalDateTime lastPlayedAt) {
        this.lastPlayedAt = lastPlayedAt;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setTotalPlaytimeSeconds(Long totalPlaytimeSeconds) {
        this.totalPlaytimeSeconds = totalPlaytimeSeconds;
    }

    public void setPlayer(PlayerJpaEntity player) {
        this.player = player;
    }
}