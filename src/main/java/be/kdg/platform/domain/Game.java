package be.kdg.platform.domain;

import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;
import be.kdg.platform.domain.valueobj.AchievementDefinitions;

import java.time.LocalDate;
import java.util.*;

public class Game {

    private GameId gameId;
    private String name;
    private String rules;
    private String pictureUrl;
    private String gameUrl;
    private String category;
    private String developedBy;
    private LocalDate createdAt;
    private int averageMinutes;
    private AchievementDefinitions achievements;

    public Game(GameId gameId,
                String name,
                String rules,
                String pictureUrl,
                String gameUrl,
                String category,
                String developedBy,
                LocalDate createdAt,
                int averageMinutes) {

        this.gameId = gameId;
        this.name = Objects.requireNonNull(name);
        this.rules = rules;
        this.pictureUrl = pictureUrl;
        this.gameUrl = Objects.requireNonNull(gameUrl);
        this.category = category;
        this.developedBy = developedBy;
        this.createdAt = createdAt;
        this.averageMinutes = averageMinutes;
    }

    public AchievementDefinition addAchievement(String name,
                                                String description,
                                                String howToUnlock) {

        var id = AchievementId.create();
        var def = new AchievementDefinition(id, name, description, howToUnlock);

        achievements.add(def);
        return def;
    }

    public void removeAchievement(AchievementId id) {
        achievements.remove(id);
    }

    public GameId getGameId() {
        return gameId;
    }

    public void setGameId(GameId gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public AchievementDefinitions getAchievements() {
        return achievements;
    }

    public void setAchievements(AchievementDefinitions achievements) {
        this.achievements = achievements;
    }

    public String getDevelopedBy() {
        return developedBy;
    }

    public int getAverageMinutes() {
        return averageMinutes;
    }
}

