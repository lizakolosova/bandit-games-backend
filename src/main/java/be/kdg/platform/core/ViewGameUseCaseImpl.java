package be.kdg.platform.core;

import be.kdg.common.valueobj.GameId;
import be.kdg.platform.domain.Game;
import be.kdg.platform.domain.exceptions.GameNotFoundException;
import be.kdg.platform.port.in.ViewGameCommand;
import be.kdg.platform.port.in.ViewGameUseCase;
import be.kdg.platform.port.out.LoadGamePort;
import org.springframework.stereotype.Service;

@Service
public class ViewGameUseCaseImpl implements ViewGameUseCase {

    private final LoadGamePort loadGamePort;

    public ViewGameUseCaseImpl(LoadGamePort loadGamePort) {
        this.loadGamePort = loadGamePort;
    }

    @Override
    public Game viewGame(ViewGameCommand command) throws GameNotFoundException {
        GameId id = GameId.of(command.gameId());
        return loadGamePort.loadById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
    }
}