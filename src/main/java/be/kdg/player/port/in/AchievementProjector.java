package be.kdg.player.port.in;

import be.kdg.player.port.in.command.ChessAchievementUnlockedProjectionCommand;
import be.kdg.player.port.in.command.TicTacToeAchievementUnlockedProjectionCommand;

public interface AchievementProjector {
    void project(TicTacToeAchievementUnlockedProjectionCommand command);
    void project(ChessAchievementUnlockedProjectionCommand command);
}

