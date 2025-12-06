package be.kdg.gameplay.port.out;

import be.kdg.gameplay.domain.GameViewProjection;

public interface AddGameViewProjectionPort {
    void add(GameViewProjection gameViewProjection);
}
