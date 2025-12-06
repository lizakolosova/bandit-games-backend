package be.kdg.player.port.in;

import be.kdg.player.domain.Player;
import be.kdg.player.port.in.command.RegisterPlayerCommand;

public interface RegisterPlayerUseCase {
    Player register(RegisterPlayerCommand command);
}
