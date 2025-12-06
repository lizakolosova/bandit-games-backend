package be.kdg.gameplay.port.in;

import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.port.in.command.CreateGameRoomCommand;

public interface CreateGameRoomUseCase {
    GameRoom create(CreateGameRoomCommand command);
}

