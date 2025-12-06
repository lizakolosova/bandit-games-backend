package be.kdg.player.core.projector;

import be.kdg.player.domain.GameProjection;
import be.kdg.player.port.in.command.GameAddedProjectionCommand;
import be.kdg.player.port.in.GameProjector;
import be.kdg.player.port.in.command.RegisterPlayerGameProjectionCommand;
import be.kdg.player.port.out.AddGameProjectionPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameProjectorImpl implements GameProjector {

    private final AddGameProjectionPort games;

    public GameProjectorImpl(AddGameProjectionPort games) {
        this.games = games;
    }
    @Override
    public void project(GameAddedProjectionCommand command) {

        GameProjection gameProjection = new GameProjection(command.gameId(), command.name(), command.pictureUrl(),
                command.category(), command.rules(), command.averageMinutes(), command.achievementCount(), command.developedBy());

        games.addGameProjection(gameProjection);
    }

    @Override
    @Transactional
    public void project(RegisterPlayerGameProjectionCommand command) {
        GameProjection projection = new GameProjection(command.gameId(), command.name(), command.pictureUrl(),
                command.category(), command.rules(), command.achievementCount(), command.averageMinutes(), command.developedBy());

        games.addGameProjection(projection);
    }
}