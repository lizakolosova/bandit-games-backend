package be.kdg.player.port.in;

import be.kdg.player.port.in.command.GameAddedProjectionCommand;
import be.kdg.player.port.in.command.RegisterPlayerGameProjectionCommand;

public interface GameProjector {
    void project(GameAddedProjectionCommand command);
    void project(RegisterPlayerGameProjectionCommand command);
}