package be.kdg.gameplay.domain.valueobj;

import java.util.UUID;

public record GameRoomId(UUID uuid) {
    public static GameRoomId of(UUID uuid) {
        return new GameRoomId(uuid);
    }
    public static GameRoomId create() {
        return new GameRoomId(UUID.randomUUID());
    }
}
