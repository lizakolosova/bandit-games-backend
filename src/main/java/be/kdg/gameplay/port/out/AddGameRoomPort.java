package be.kdg.gameplay.port.out;

import be.kdg.gameplay.domain.GameRoom;

public interface AddGameRoomPort {
    GameRoom add(GameRoom room);
}

