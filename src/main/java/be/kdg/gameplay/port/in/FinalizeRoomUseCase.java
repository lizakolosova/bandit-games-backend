package be.kdg.gameplay.port.in;

import be.kdg.gameplay.domain.GameRoom;

public interface FinalizeRoomUseCase {
    GameRoom finalize(FinalizeRoomCommand command);
}