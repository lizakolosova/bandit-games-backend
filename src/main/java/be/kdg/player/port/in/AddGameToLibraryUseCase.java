package be.kdg.player.port.in;

import be.kdg.player.domain.GameLibrary;

public interface AddGameToLibraryUseCase {
    GameLibrary addGameToLibrary(AddGameToLibraryCommand command);
}

