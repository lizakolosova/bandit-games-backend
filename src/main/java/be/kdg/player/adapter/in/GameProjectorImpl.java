package be.kdg.player.adapter.in;

import be.kdg.player.adapter.out.GameProjectionJpaEntity;
import be.kdg.player.adapter.out.GameProjectionJpaRepository;
import be.kdg.player.domain.GameProjection;
import be.kdg.player.port.in.GameAddedProjectionCommand;
import be.kdg.player.port.in.GameProjector;
import be.kdg.player.port.out.AddGameProjectionPort;
import be.kdg.player.port.out.LoadGameProjectionPort;
import org.springframework.stereotype.Component;

@Component
public class GameProjectorImpl implements GameProjector {

    private final AddGameProjectionPort games;

    public GameProjectorImpl(AddGameProjectionPort games) {
        this.games = games;
    }

    @Override
    public void project(GameAddedProjectionCommand command) {

        GameProjection gameProjection = new GameProjection(
                command.gameId(),
                command.name(),
                command.pictureUrl(),
                command.category(),
                command.rules(),
                command.averageMinutes(),
                command.achievementCount(),
                command.developedBy()
        );

        games.addGameProjection(gameProjection);
    }
}