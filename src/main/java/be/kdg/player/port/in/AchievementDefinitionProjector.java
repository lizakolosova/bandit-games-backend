package be.kdg.player.port.in;

import be.kdg.player.port.in.command.AchievementAddedProjectionCommand;

public interface AchievementDefinitionProjector {
    void project(AchievementAddedProjectionCommand command);
}
