package be.kdg.player.adapter.out;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "player_achievement", schema = "kdg_player")
public class PlayerAchievementJpaEntity {

    @Id
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerJpaEntity player;

    private UUID achievementId;
    private UUID gameId;

    private LocalDateTime unlockedAt;

    protected PlayerAchievementJpaEntity() {}

    public PlayerAchievementJpaEntity(UUID uuid,
                                      PlayerJpaEntity player,
                                      UUID achievementId,
                                      UUID gameId,
                                      LocalDateTime unlockedAt) {
        this.uuid = uuid;
        this.player = player;
        this.achievementId = achievementId;
        this.gameId = gameId;
        this.unlockedAt = unlockedAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PlayerJpaEntity getPlayer() {
        return player;
    }

    public UUID getAchievementId() {
        return achievementId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public LocalDateTime getUnlockedAt() {
        return unlockedAt;
    }
}