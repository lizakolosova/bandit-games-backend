package be.kdg.player.port.out;

import be.kdg.player.domain.GameLibrary;

import java.util.Optional;
import java.util.UUID;

public interface LoadGameLibraryPort {
    Optional<GameLibrary> loadByGameId(UUID gameId);
}

