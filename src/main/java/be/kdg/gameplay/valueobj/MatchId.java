package be.kdg.gameplay.valueobj;

import be.kdg.common.valueobj.GameId;

import java.util.UUID;

public record MatchId(UUID uuid) {
    public static MatchId of(UUID uuid) {
        return new MatchId(uuid);
    }
    public static MatchId create() {
        return new MatchId(UUID.randomUUID());
    }
}
