package be.kdg.platform.domain;

import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;
import be.kdg.platform.domain.valueobj.AchievementDefinitions;

import java.time.LocalDateTime;
import java.util.*;

public class Game {

    private GameId gameId;
    private String name;
    private String description;
    private String pictureUrl;
    private String gameUrl;
    private String category;
    private LocalDateTime createdAt;
    private AchievementDefinitions achievements;

    public Game(GameId gameId,
                String name,
                String description,
                String pictureUrl,
                String gameUrl,
                String category) {

        this.gameId = gameId;
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.gameUrl = Objects.requireNonNull(gameUrl);
        this.category = category;
        this.createdAt = LocalDateTime.now();
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
}

