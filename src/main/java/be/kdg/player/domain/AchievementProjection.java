package be.kdg.player.domain;

import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;

public class AchievementProjection {

    private final AchievementId achievementId;
    private final String name;
    private final String description;
    private final GameId gameId;

    public AchievementProjection(AchievementId achievementId,
                                 String name,
                                 String description,
                                 GameId gameId) {

        this.achievementId = achievementId;
        this.name = name;
        this.description = description;
        this.gameId = gameId;
    }

    public AchievementId getAchievementId() {
        return achievementId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GameId getGameId() {
        return gameId;
    }
}