package be.kdg.player.port.in;

import be.kdg.player.domain.GameLibrary;
import be.kdg.player.port.in.command.AddGameToLibraryCommand;

public interface AddGameToLibraryUseCase {
    GameLibrary addGameToLibrary(AddGameToLibraryCommand command);
}

