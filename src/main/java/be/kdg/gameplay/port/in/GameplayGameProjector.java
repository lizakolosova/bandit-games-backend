package be.kdg.gameplay.port.in;

import be.kdg.gameplay.port.in.command.RegisterGameViewProjectionCommand;

public interface GameplayGameProjector {
    void project(RegisterGameViewProjectionCommand command);
}

