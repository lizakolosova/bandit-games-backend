package be.kdg.player.port.in;

import be.kdg.player.domain.Player;

public interface RegisterPlayerUseCase {
    Player register(RegisterPlayerCommand command);
}
