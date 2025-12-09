package be.kdg.gameplay.port.in;

import be.kdg.gameplay.port.in.command.RegisterGameViewProjectionCommand;

public interface RegisterGameProjector {
    void project(RegisterGameViewProjectionCommand command);
}
