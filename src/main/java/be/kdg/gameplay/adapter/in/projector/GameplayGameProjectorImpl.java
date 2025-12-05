package be.kdg.gameplay.adapter.in.projector;

import be.kdg.common.valueobj.GameId;
import be.kdg.gameplay.domain.GameViewProjection;
import be.kdg.gameplay.port.in.GameplayGameProjector;
import be.kdg.gameplay.port.in.command.RegisterGameViewProjectionCommand;
import be.kdg.gameplay.port.out.AddGameViewProjectionPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GameplayGameProjectorImpl implements GameplayGameProjector {

    private final AddGameViewProjectionPort addGameViewProjectionPort;

    public GameplayGameProjectorImpl(AddGameViewProjectionPort addGameViewProjectionPort) {
        this.addGameViewProjectionPort = addGameViewProjectionPort;
    }

    @Override
    @Transactional
    public void project(RegisterGameViewProjectionCommand c) {
        GameViewProjection projection = new GameViewProjection(
                GameId.of(UUID.fromString(c.gameId())),
                c.name()
        );

        addGameViewProjectionPort.add(projection);
    }
}

