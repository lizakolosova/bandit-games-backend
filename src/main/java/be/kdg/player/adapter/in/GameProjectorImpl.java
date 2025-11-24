package be.kdg.player.adapter.in;

import be.kdg.player.adapter.out.GameProjectionJpaEntity;
import be.kdg.player.adapter.out.GameProjectionJpaRepository;
import be.kdg.player.port.in.GameAddedProjectionCommand;
import be.kdg.player.port.in.GameProjector;
import org.springframework.stereotype.Component;

@Component
public class GameProjectorImpl implements GameProjector {

    private final GameProjectionJpaRepository gameProjections;

    public GameProjectorImpl(GameProjectionJpaRepository gameProjections) {
        this.gameProjections = gameProjections;
    }

    @Override
    public void project(GameAddedProjectionCommand command) {

        GameProjectionJpaEntity entity = new GameProjectionJpaEntity(
                command.gameId(),
                command.name(),
                command.pictureUrl(),
                command.category(),
                command.rules(),
                command.averageMinutes(),
                command.achievementCount(),
                command.developedBy()
        );

        gameProjections.save(entity);
    }
}