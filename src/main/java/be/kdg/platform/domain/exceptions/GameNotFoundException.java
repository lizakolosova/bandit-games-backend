package be.kdg.platform.domain.exceptions;

import be.kdg.common.valueobj.GameId;

public class GameNotFoundException extends Exception {

    public GameNotFoundException(GameId id) {
        super("Game not found with id: " + id.uuid());
    }
}

