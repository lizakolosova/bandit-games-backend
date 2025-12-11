package be.kdg.player.port.in;

import be.kdg.player.domain.GameLibrary;
import be.kdg.player.port.in.command.LoadSingleGameLibraryCommand;

public interface LoadSingleGameLibraryUseCase {
    GameLibrary loadGame(LoadSingleGameLibraryCommand command);
}