package be.kdg.platform.core;

import be.kdg.common.valueobj.GameId;
import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.AddGameCommand;
import be.kdg.platform.port.in.AddGameUseCase;
import be.kdg.platform.port.out.AddGamePort;
import org.springframework.stereotype.Service;

@Service
public class AddGameUseCaseImpl implements AddGameUseCase {

    private final AddGamePort addGamePort;

    public AddGameUseCaseImpl(AddGamePort addGamePort) {
        this.addGamePort = addGamePort;
    }

    @Override
    public Game createGame(AddGameCommand command) {

        Game game = new Game(
                GameId.create(),
                command.name(),
                command.rules(),
                command.pictureUrl(),
                command.gameUrl(),
                command.category(),
                command.developedBy(),
                command.createdAt(),
                command.averageMinutes()
        );

        return addGamePort.add(game);
    }
}
