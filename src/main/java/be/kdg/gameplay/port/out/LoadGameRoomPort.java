package be.kdg.gameplay.port.out;

import be.kdg.gameplay.domain.GameRoom;
import be.kdg.gameplay.domain.valueobj.GameRoomId;

import java.util.UUID;

public interface LoadGameRoomPort {
    GameRoom loadById(GameRoomId id);
    GameRoom findByPlayers(UUID hostPlayerId, UUID opponentPlayerId);
}

