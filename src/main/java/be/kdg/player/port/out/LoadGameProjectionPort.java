package be.kdg.player.port.out;

import be.kdg.player.domain.GameProjection;

import java.util.Optional;
import java.util.UUID;

public interface LoadGameProjectionPort {
    GameProjection loadProjection(UUID gameId);
}
