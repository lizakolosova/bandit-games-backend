package be.kdg.gameplay.domain;

import be.kdg.common.valueobj.GameId;

public class GameViewProjection {
    private GameId gameId;
    private String name;

    public GameViewProjection(GameId gameId, String name) {
        this.gameId = gameId;
        this.name = name;
    }
    public GameId getGameId() {
        return gameId;
    }

    public String getName() {
        return name;
    }
}
