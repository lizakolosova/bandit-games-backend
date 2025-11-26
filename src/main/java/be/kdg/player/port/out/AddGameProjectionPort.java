package be.kdg.player.port.out;

import be.kdg.player.domain.GameProjection;

public interface AddGameProjectionPort {
    void addGameProjection(GameProjection gameProjection);
}
