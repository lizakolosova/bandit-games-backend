package be.kdg.gameplay.port.in;

import be.kdg.gameplay.port.in.command.TicTacToeGameCreatedProjectionCommand;
import be.kdg.gameplay.port.in.command.TicTacToeGameEndedProjectionCommand;
import be.kdg.gameplay.port.in.command.TicTacToeGameUpdatedProjectionCommand;

public interface TicTacToeGameProjector {
    void project(TicTacToeGameCreatedProjectionCommand command);
    void project(TicTacToeGameEndedProjectionCommand command);
    void project(TicTacToeGameUpdatedProjectionCommand command);
}

