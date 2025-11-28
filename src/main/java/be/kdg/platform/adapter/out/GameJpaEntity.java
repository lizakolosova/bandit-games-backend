package be.kdg.platform.adapter.out;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "games", schema = "kdg_platform")
public class GameJpaEntity {

    @Id
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column
    private String rules;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "game_url", nullable = false)
    private String gameUrl;

    @Column
    private String category;

    @Column
    private String developedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    private int averageMinutes;

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<AchievementDefinitionJpaEntity> achievements = new ArrayList<>();

    public GameJpaEntity() {
    }

    public GameJpaEntity(UUID uuid,
                         String name,
                         String rules,
                         String pictureUrl,
                         String gameUrl,
                         String category,
                         String developedBy,
                         LocalDate createdAt,
                         int averageMinutes) {
        this.uuid = uuid;
        this.name = name;
        this.rules = rules;
        this.pictureUrl = pictureUrl;
        this.gameUrl = gameUrl;
        this.category = category;
        this.developedBy = developedBy;
        this.createdAt = createdAt;
        this.averageMinutes = averageMinutes;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getRules() {
        return rules;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getDevelopedBy() {
        return developedBy;
    }

    public int getAverageMinutes() {
        return averageMinutes;
    }

    public List<AchievementDefinitionJpaEntity> getAchievements() {
        return achievements;
    }
}

