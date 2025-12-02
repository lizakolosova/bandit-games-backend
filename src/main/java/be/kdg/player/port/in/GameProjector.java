package be.kdg.player.port.in;

import be.kdg.player.port.in.command.GameAddedProjectionCommand;

public interface GameProjector {
    void project(GameAddedProjectionCommand command);
}