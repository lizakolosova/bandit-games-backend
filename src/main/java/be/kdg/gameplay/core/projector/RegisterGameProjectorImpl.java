package be.kdg.gameplay.core.projector;

import be.kdg.common.valueobj.GameId;
import be.kdg.gameplay.domain.GameViewProjection;
import be.kdg.gameplay.port.in.RegisterGameProjector;
import be.kdg.gameplay.port.in.command.RegisterGameViewProjectionCommand;
import be.kdg.gameplay.port.out.AddGameViewProjectionPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RegisterGameProjectorImpl implements RegisterGameProjector {

    private final AddGameViewProjectionPort addGameViewProjectionPort;

    public RegisterGameProjectorImpl(AddGameViewProjectionPort addGameViewProjectionPort) {
        this.addGameViewProjectionPort = addGameViewProjectionPort;
    }

    @Override
    @Transactional
    public void project(RegisterGameViewProjectionCommand command) {
        GameViewProjection projection = new GameViewProjection(GameId.of(command.gameId()), command.name());
        addGameViewProjectionPort.add(projection);
    }
}
