package be.kdg.platform.port.in;

import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.command.AddGameWithAchievementsCommand;

public interface AddGameWithAchievementsUseCase {
    Game create(AddGameWithAchievementsCommand command);
}
