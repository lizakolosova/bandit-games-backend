package be.kdg.player.port.out;

import be.kdg.player.domain.AchievementProjection;

public interface AddAchievementProjectionPort {
    void add(AchievementProjection projection);
}