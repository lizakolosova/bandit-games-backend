package be.kdg.gameplay.port.in;

import be.kdg.gameplay.domain.GameRoom;

public interface CreateGameRoomUseCase {
    GameRoom create(CreateGameRoomCommand command);
}

