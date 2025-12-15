package be.kdg.gameplay.domain.valueobj;

import java.util.UUID;

public record GameMatchId(UUID uuid) {
    public static GameMatchId of(UUID uuid) {
        return new GameMatchId(uuid);
    }
    public static GameMatchId create() {
        return new GameMatchId(UUID.randomUUID());
    }
}
