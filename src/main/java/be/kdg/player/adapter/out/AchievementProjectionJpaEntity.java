package be.kdg.player.adapter.out;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "achievement_projection", schema = "kdg_player")
public class AchievementProjectionJpaEntity {

    @Id
    private UUID achievementId;

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private GameProjectionJpaEntity gameProjection;

    protected AchievementProjectionJpaEntity() { }

    public AchievementProjectionJpaEntity(UUID achievementId,
                                          String name,
                                          String description,
                                          GameProjectionJpaEntity gameProjection) {
        this.achievementId = achievementId;
        this.name = name;
        this.description = description;
        this.gameProjection = gameProjection;
    }

    public UUID getAchievementId() {
        return achievementId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GameProjectionJpaEntity getGameProjection() {
        return gameProjection;
    }
}