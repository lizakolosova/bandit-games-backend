package be.kdg.gameplay.core;

import be.kdg.common.valueobj.GameId;
import be.kdg.common.valueobj.PlayerId;
import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.port.in.CreateGameRoomCommand;
import be.kdg.gameplay.port.in.CreateGameRoomUseCase;
import be.kdg.gameplay.port.out.AddGameRoomPort;
import org.springframework.stereotype.Service;

@Service
public class CreateGameRoomUseCaseImpl implements CreateGameRoomUseCase {

    private final AddGameRoomPort addGameRoomPort;

    public CreateGameRoomUseCaseImpl(AddGameRoomPort addGameRoomPort) {
        this.addGameRoomPort = addGameRoomPort;
    }

    @Override
    public GameRoom create(CreateGameRoomCommand command) {

        GameRoom room = new GameRoom(
                GameId.of(command.gameId()),
                PlayerId.of(command.hostPlayerId()),
                PlayerId.of(command.invitedPlayerId()),
                command.gameRoomType()
        );

        return addGameRoomPort.add(room);
    }
}