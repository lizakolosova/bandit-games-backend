package be.kdg.player.domain;

import be.kdg.common.valueobj.AchievementId;
import be.kdg.common.valueobj.GameId;
import be.kdg.player.domain.valueobj.PlayerAchievementId;
import be.kdg.common.valueobj.PlayerId;

import java.time.LocalDateTime;

public class PlayerAchievement {

    private final PlayerAchievementId playerAchievementId;
    private final PlayerId playerId;
    private final AchievementId achievementId;
    private final GameId gameId;
    private final LocalDateTime unlockedAt;

    public PlayerAchievement(PlayerId playerId, AchievementId achievementId, GameId gameId) {
        this(PlayerAchievementId.create(), playerId, achievementId, gameId, LocalDateTime.now());
    }

    public PlayerAchievement(PlayerAchievementId playerAchievementId, PlayerId playerId, AchievementId achievementId, GameId gameId, LocalDateTime unlockedAt) {
        this.playerAchievementId = playerAchievementId;
        this.playerId = playerId;
        this.achievementId = achievementId;
        this.gameId = gameId;
        this.unlockedAt = unlockedAt;
    }

    public PlayerAchievementId getPlayerAchievementId() {
        return playerAchievementId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public AchievementId getAchievementId() {
        return achievementId;
    }

    public GameId getGameId() {
        return gameId;
    }

    public LocalDateTime getUnlockedAt() {
        return unlockedAt;
    }
}

