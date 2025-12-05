package be.kdg.gameplay.port.in;

import be.kdg.gameplay.domain.GameRoom;

public interface StartMatchUseCase {
    GameRoom start(StartMatchCommand command);
}
