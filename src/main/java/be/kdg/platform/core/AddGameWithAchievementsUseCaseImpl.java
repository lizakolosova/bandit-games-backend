package be.kdg.platform.core;

import be.kdg.platform.adapter.out.PlatformEventPublisher;
import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.*;
import be.kdg.platform.port.in.command.AddGameWithAchievementsCommand;
import be.kdg.platform.port.out.AddGamePort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AddGameWithAchievementsUseCaseImpl implements AddGameWithAchievementsUseCase {

    private final AddGamePort addGamePort;
    private final PlatformEventPublisher eventPublisher;

    public AddGameWithAchievementsUseCaseImpl(AddGamePort addGamePort,
                                              PlatformEventPublisher eventPublisher) {
        this.addGamePort = addGamePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Game create(AddGameWithAchievementsCommand command) {
        Game game = new Game(command.name(), command.rules(), command.pictureUrl(), command.gameUrl(),
                command.category(), command.developedBy(), command.createdAt(), command.averageMinutes());

        if (command.achievements() != null) {
            command.achievements().forEach(a ->
                    game.addAchievement(a.name(), a.description())
            );
        }
        Game saved = addGamePort.add(game);
        eventPublisher.publishEvents(saved.pullDomainEvents());
        return saved;
    }
}