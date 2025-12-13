package be.kdg.player.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class GameProjection {

    private final UUID gameId;
    private final String name;
    private final String pictureUrl;
    private final String category;
    private final String rules;
    private final int achievementCount;
    private final int averageMinutes;
    private final String developedBy;
    private BigDecimal price;


    public GameProjection(UUID gameId,
                          String name,
                          String pictureUrl,
                          String category,
                          String rules,
                          int achievementCount,
                          int averageMinutes,
                          String developedBy, BigDecimal price) {

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

    public GameProjection(UUID gameId,
                          String name,
                          String pictureUrl,
                          String category,
                          String rules,
                          int achievementCount,
                          int averageMinutes,
                          String developedBy) {

        this.gameId = gameId;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.category = category;
        this.rules = rules;
        this.achievementCount = achievementCount;
        this.averageMinutes = averageMinutes;
        this.developedBy = developedBy;
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

}
