package be.kdg.player.port.in;

import be.kdg.player.port.in.command.UnifiedAchievementUnlockedProjectionCommand;

public interface UnifiedAchievementProjector {
    void projectAchievementUnlocked(UnifiedAchievementUnlockedProjectionCommand command);
}
