package be.kdg.platform.core;

import be.kdg.common.valueobj.GameId;
import be.kdg.platform.adapter.out.PlatformEventPublisher;
import be.kdg.platform.domain.Game;
import be.kdg.platform.domain.exceptions.GameNotFoundException;
import be.kdg.platform.port.in.ApproveGameUseCase;
import be.kdg.platform.port.in.command.ApproveGameCommand;
import be.kdg.platform.port.out.AddGamePort;
import be.kdg.platform.port.out.LoadGamePort;
import org.springframework.stereotype.Service;

@Service
public class ApproveGameUseCaseImpl implements ApproveGameUseCase {

    private final LoadGamePort loadGamePort;
    private final AddGamePort addGamePort;
    private final PlatformEventPublisher eventPublisher;

    public ApproveGameUseCaseImpl(LoadGamePort loadGamePort, AddGamePort addGamePort, PlatformEventPublisher eventPublisher) {
        this.loadGamePort = loadGamePort;
        this.addGamePort = addGamePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void approve(ApproveGameCommand command) throws GameNotFoundException {
        Game game = loadGamePort.loadById(GameId.of(command.gameId())).orElseThrow(() ->
                new GameNotFoundException(GameId.of(command.gameId())));
        game.approve();
        addGamePort.add(game);
        eventPublisher.publishEvents(game.pullDomainEvents());
    }
}

