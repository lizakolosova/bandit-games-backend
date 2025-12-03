package be.kdg.gameplay.port.out;

import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomId;

public interface LoadGameRoomPort {
    GameRoom loadById(GameRoomId id);
}

