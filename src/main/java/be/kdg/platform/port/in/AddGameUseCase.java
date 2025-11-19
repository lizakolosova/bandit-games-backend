package be.kdg.platform.port.in;

import be.kdg.platform.domain.Game;

public interface AddGameUseCase {
    Game createGame(AddGameCommand command);
}
