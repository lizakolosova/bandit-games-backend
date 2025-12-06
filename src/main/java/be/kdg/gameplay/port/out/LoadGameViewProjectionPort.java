package be.kdg.gameplay.port.out;

import be.kdg.gameplay.domain.GameViewProjection;

public interface LoadGameViewProjectionPort {
    GameViewProjection findByName(String name);
}
