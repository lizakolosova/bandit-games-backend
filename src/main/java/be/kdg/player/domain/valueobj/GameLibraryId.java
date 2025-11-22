package be.kdg.player.domain.valueobj;

import java.util.UUID;

public record GameLibraryId(UUID uuid) {
    public static GameLibraryId of(UUID uuid) {
        return new GameLibraryId(uuid);
    }
    public static GameLibraryId create() {
        return new GameLibraryId(UUID.randomUUID());
    }
}
