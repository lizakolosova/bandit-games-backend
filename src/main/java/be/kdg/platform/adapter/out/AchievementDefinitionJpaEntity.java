package be.kdg.platform.adapter.out;

import be.kdg.platform.domain.Game;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "achievement_definitions", schema = "kdg_platform")
public class AchievementDefinitionJpaEntity {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameJpaEntity game;

    protected AchievementDefinitionJpaEntity() {
    }

    public AchievementDefinitionJpaEntity(UUID uuid,
                                          String name,
                                          String description,
                                          GameJpaEntity game) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.game = game;

    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
