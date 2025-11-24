package be.kdg.platform.core;
import be.kdg.platform.adapter.out.PlatformEventPublisher;
import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.AddGameCommand;
import be.kdg.platform.port.in.AddGameUseCase;
import be.kdg.platform.port.out.AddGamePort;
import org.springframework.stereotype.Service;

@Service
public class AddGameUseCaseImpl implements AddGameUseCase {

    private final AddGamePort addGamePort;
    private final PlatformEventPublisher eventPublisher;

    public AddGameUseCaseImpl(AddGamePort addGamePort, PlatformEventPublisher eventPublisher) {
        this.addGamePort = addGamePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Game createGame(AddGameCommand command) {

        Game game = new Game(
                command.name(),
                command.rules(),
                command.pictureUrl(),
                command.gameUrl(),
                command.category(),
                command.developedBy(),
                command.createdAt(),
                command.averageMinutes()
        );

        eventPublisher.publishEvents(game.pullDomainEvents());
        return addGamePort.add(game);
    }
}