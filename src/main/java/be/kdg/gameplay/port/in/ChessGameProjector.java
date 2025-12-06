package be.kdg.gameplay.port.in;

import be.kdg.gameplay.port.in.command.ChessGameCreatedProjectionCommand;
import be.kdg.gameplay.port.in.command.ChessGameEndedProjectionCommand;
import be.kdg.gameplay.port.in.command.ChessGameUpdatedProjectionCommand;
import be.kdg.gameplay.port.in.command.RegisterGameViewProjectionCommand;

public interface ChessGameProjector {
    void project(ChessGameCreatedProjectionCommand command);
    void project(ChessGameUpdatedProjectionCommand command);
    void project(ChessGameEndedProjectionCommand command);
    void project(RegisterGameViewProjectionCommand command);
}