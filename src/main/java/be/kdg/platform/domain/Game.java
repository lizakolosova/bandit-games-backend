package be.kdg.platform.domain;

import be.kdg.common.events.DomainEvent;
import be.kdg.common.events.GameAddedEvent;
import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;

import java.math.BigDecimal;
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
    private List<AchievementDefinition> achievements;
    private BigDecimal price;
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    public Game(String name, String rules, String pictureUrl, String gameUrl, String category, String developedBy,
                LocalDate createdAt, int averageMinutes) {
        this(GameId.create(), Objects.requireNonNull(name), rules, pictureUrl, gameUrl, category, developedBy, createdAt, averageMinutes);
        registerEvent(new GameAddedEvent(
                gameId.uuid(),
                name,
                rules,
                pictureUrl,
                category,
                developedBy,
                createdAt,
                averageMinutes,
                achievements == null ? 0 : achievements.size()
        ));
    }

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
        this.achievements = new ArrayList<>();
    }

    public AchievementDefinition addAchievement(String name,
                                                String description,
                                                String howToUnlock) {

        AchievementId id = AchievementId.create();
        AchievementDefinition def = new AchievementDefinition(id, name, description, howToUnlock);
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

    public List<AchievementDefinition> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<AchievementDefinition> achievements) {
        this.achievements = achievements;
    }

    public String getDevelopedBy() {
        return developedBy;
    }

    public int getAverageMinutes() {
        return averageMinutes;
    }

    public List<DomainEvent> pullDomainEvents() {
        var copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }
}

