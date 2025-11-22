package be.kdg.player.port.in;

import be.kdg.player.domain.GameLibrary;

import java.util.List;

public interface LoadGameLibraryUseCase {
    List<GameLibrary> loadLibrary(LoadGameLibraryCommand command);
}
