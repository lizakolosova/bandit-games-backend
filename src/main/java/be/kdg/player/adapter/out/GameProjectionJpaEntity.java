package be.kdg.player.adapter.out;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "game_projection", schema = "kdg_player")
public class GameProjectionJpaEntity {

    @Id
    private UUID gameId;
    private String name;
    private String pictureUrl;
    private String category;
    private String rules;
    private int achievementCount;
    private int averageMinutes;
    private String developedBy;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "gameProjection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AchievementProjectionJpaEntity> achievementProjections = new ArrayList<>();

    protected GameProjectionJpaEntity() { }

    public GameProjectionJpaEntity(UUID gameId,
                                   String name,
                                   String pictureUrl,
                                   String category,
                                   String rules,
                                   int achievementCount,
                                   int averageMinutes,
                                   String developedBy,
                                   BigDecimal price) {
        this.gameId = gameId;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.category = category;
        this.rules = rules;
        this.achievementCount = achievementCount;
        this.averageMinutes = averageMinutes;
        this.developedBy = developedBy;
        this.price = price;
    }

    public UUID getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getCategory() {
        return category;
    }

    public int getAchievementCount() {
        return achievementCount;
    }

    public int getAverageMinutes() {
        return averageMinutes;
    }

    public String getDevelopedBy() {
        return developedBy;
    }

    public String getRules() {
        return rules;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}