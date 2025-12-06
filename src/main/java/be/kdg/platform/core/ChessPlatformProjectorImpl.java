package be.kdg.platform.core;

import be.kdg.common.events.chess.AchievementEntry;
import be.kdg.common.valueobj.GameId;
import be.kdg.platform.adapter.out.PlatformEventPublisher;
import be.kdg.platform.domain.Game;
import be.kdg.platform.port.in.command.RegisterChessGameProjectionCommand;
import be.kdg.platform.port.in.ChessPlatformProjector;
import be.kdg.platform.port.out.AddGamePort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChessPlatformProjectorImpl implements ChessPlatformProjector {

    private final AddGamePort addGamePort;
    private final PlatformEventPublisher eventPublisher;

    public ChessPlatformProjectorImpl(AddGamePort addGamePort, PlatformEventPublisher eventPublisher) {
        this.addGamePort = addGamePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void project(RegisterChessGameProjectionCommand command) {

        Game game = new Game(
                GameId.of(command.registrationId()),
                "Chess",
               "Standard rules",
                command.pictureUrl(),
                command.frontendUrl(),
                "Board Game",
                "Chess Platform",
                LocalDate.now(),
                30
        );

        List<AchievementEntry> achievements = command.achievements();
        if (command.achievements() != null) {
            achievements.forEach(a ->
                    game.addAchievement(a.code(), a.description(), a.description())
            );
        }
        eventPublisher.publishEvents(game.pullDomainEvents());
        addGamePort.add(game);
    }
}
