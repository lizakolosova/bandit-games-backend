package be.kdg.common.valueobj;

import java.util.UUID;

public record GameId(UUID uuid) {
    public static GameId of(UUID uuid) {
        return new GameId(uuid);
    }
    public static GameId create() {
        return new GameId(UUID.randomUUID());
    }
}
