package be.kdg.gameplay.port.out;

import be.kdg.gameplay.domain.GameRoom;

public interface UpdateGameRoomPort {
    GameRoom update(GameRoom room);
}

