package be.kdg.player.port.out;

import be.kdg.common.valueobj.GameId;
import be.kdg.player.domain.GameProjection;

import java.util.Optional;
import java.util.UUID;

public interface LoadGameProjectionPort {
    GameProjection loadProjection(GameId gameId);
}
