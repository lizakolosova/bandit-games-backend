package be.kdg.player.port.out;

import be.kdg.common.valueobj.GameId;
import be.kdg.player.domain.AchievementProjection;

import java.util.Optional;

public interface LoadAchievementProjectionPort {
    Optional<AchievementProjection> loadByGameIdAndType(GameId gameId, String achievementType);
}
