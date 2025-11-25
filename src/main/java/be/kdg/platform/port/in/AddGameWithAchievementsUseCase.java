package be.kdg.platform.port.in;

import be.kdg.platform.domain.Game;

public interface AddGameWithAchievementsUseCase {
    Game create(AddGameWithAchievementsCommand command);
}
