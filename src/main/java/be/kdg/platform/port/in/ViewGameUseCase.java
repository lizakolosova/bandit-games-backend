package be.kdg.platform.port.in;

import be.kdg.platform.domain.Game;
import be.kdg.platform.domain.exceptions.GameNotFoundException;

public interface ViewGameUseCase {
    Game viewGame(ViewGameCommand command) throws GameNotFoundException;
}
